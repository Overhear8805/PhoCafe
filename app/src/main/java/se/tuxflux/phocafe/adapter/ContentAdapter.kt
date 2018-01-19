package se.tuxflux.phocafe.adapter

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import android.view.ViewGroup
import com.prof.rssparser.Article
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.view_item.view.*
import se.tuxflux.phocafe.R
import se.tuxflux.phocafe.utility.CircleTransform
import se.tuxflux.phocafe.utility.inflate
import se.tuxflux.phocafe.utility.loadUrl
import timber.log.Timber
import kotlin.properties.Delegates


class ContentAdapter : RecyclerView.Adapter<ContentAdapter.ViewHolder>(), AutoUpdatableAdapter {

    val fooImages: MutableList<String> = mutableListOf()
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
            val text = article.description.orEmpty()
            val terseText = String()

            Timber.i("Viewing %s", article.link)
            imageView.loadUrl("https://oresundstartups.com/wp-content/uploads/2013/01/FooCafe_logo_650x320.jpg")
            title.text = article.title
            link.text = article.link
            pubDate.text = article.pubDate.toString()
            terseDescription.text = terseText
            fullDescription.text = Html.fromHtml(text)

            link.setOnClickListener {
                clickSubject.onNext(article)
            }

            article.image.let {
                Picasso.with(context).load(article.image).transform(CircleTransform()).into(imageView)
            }


        }
    }

}
