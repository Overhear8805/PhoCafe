package se.tuxflux.phocafe.adapter

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import android.view.ViewGroup
import com.prof.rssparser.Article
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.view_item.view.*
import org.jsoup.Jsoup
import se.tuxflux.phocafe.R
import se.tuxflux.phocafe.inflate
import se.tuxflux.phocafe.loadUrl
import timber.log.Timber
import kotlin.properties.Delegates


class ContentAdapter : RecyclerView.Adapter<ContentAdapter.ViewHolder>(), AutoUpdatableAdapter {
    private val clickSubject = PublishSubject.create<Article>()
    val clickEvent: Observable<Article> = clickSubject

    var mExpandedPosition = -1
    var previousExpandedPosition = -1

    var index: Int = 0

    var items: MutableList<Article> by Delegates.observable(mutableListOf()) { prop, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    fun add(article: Article) {
        items.add(article)
        notifyItemChanged(items.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.view_item))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isExpanded = position == mExpandedPosition
        holder.itemView.fullDescription.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.itemView.terseDescription.visibility = if (isExpanded) View.GONE else View.VISIBLE
        holder.itemView.isActivated = isExpanded

        if (isExpanded) previousExpandedPosition = position
        holder.itemView.setOnClickListener {
            mExpandedPosition = if (isExpanded) -1 else position
            notifyItemChanged(previousExpandedPosition)
            notifyItemChanged(position)
        }
        holder.bind(items[position])
        index = position
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(article: Article) = with(itemView) {
            Timber.i("Viewing %s", article.link)
            imageView.loadUrl("https://oresundstartups.com/wp-content/uploads/2013/01/FooCafe_logo_650x320.jpg")
            title.text = article.title
            link.text = article.link
            pubDate.text = article.pubDate.toString()
            terseDescription.text = Html.fromHtml(article.description.orEmpty().substring(0, 240).plus("..."))
            fullDescription.text = Html.fromHtml(article.description.orEmpty())


            link.setOnClickListener {
                clickSubject.onNext(article)
            }

            Observable.create<Any> { emitter ->
                val doc = Jsoup.connect("http://foocafe.org/malmoe/events").get()
                Timber.i(doc.title())
                val newsHeadlines = doc.select(".cover")
                newsHeadlines.forEach { e ->
                    Timber.i(e.attr("src"))
                }


                emitter.onNext(newsHeadlines[index].attr("src"))
                emitter.onComplete()
            }.cache().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ url ->
                        val loadUrl: String = "http://foocafe.org/".plus(url)
                        Timber.i(loadUrl)
                        val dimension = if (width > 0) width else 1
                        Picasso.with(context).load(loadUrl).resize(dimension, dimension).centerInside().into(imageView)
                    })


        }
    }

}
