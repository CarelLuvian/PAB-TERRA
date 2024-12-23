import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.terra.R
import com.example.tokopestisida.TokoPestisida

class TokoAdapter(private val tokoList: List<TokoPestisida.Toko>) :
    RecyclerView.Adapter<TokoAdapter.TokoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TokoViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_toko, parent, false)
        return TokoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TokoViewHolder, position: Int) {
        val toko: TokoPestisida.Toko = tokoList[position]
        holder.namaToko.text = toko.nama
        holder.alamatToko.text = toko.alamat

        // Jika `imageUrl` adalah resource ID lokal
        holder.iconToko.setImageResource(toko.imageUrl)

        // Menangani klik pada alamat toko untuk membuka Google Maps
        holder.namaToko.setOnClickListener {
            val address = toko.nama
            val gmapsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=${Uri.encode(address)}"))
            gmapsIntent.setPackage("com.google.android.apps.maps")

            val resolvedActivity = gmapsIntent.resolveActivity(holder.itemView.context.packageManager)
            if (resolvedActivity != null) {
                holder.itemView.context.startActivity(gmapsIntent)
            } else {
                // Fallback: Buka alamat di browser jika Google Maps tidak tersedia
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(address)}"))
                holder.itemView.context.startActivity(browserIntent)
            }
        }
    }

    override fun getItemCount(): Int {
        return tokoList.size
    }

    class TokoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var namaToko: TextView = itemView.findViewById(R.id.namaToko)
        var alamatToko: TextView = itemView.findViewById(R.id.alamatToko)
        var iconToko: ImageView = itemView.findViewById(R.id.iconToko)
    }
}
