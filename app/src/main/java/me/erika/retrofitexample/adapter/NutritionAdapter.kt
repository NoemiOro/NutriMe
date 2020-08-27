package me.erika.retrofitexample.adapter

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.nutrients_view.view.*
import me.erika.retrofitexample.R
import me.erika.retrofitexample.utilities.inflate
import me.erika.retrofitexample.repository.NutrientsDTO

class NutritionAdapter:
    RecyclerView.Adapter<NutritionAdapter.ItemViewHolder>(){

    var mNutrients = ArrayList<NutrientsDTO?>()

    fun setNutritionFacts(nutrients: ArrayList<NutrientsDTO?>) {
         mNutrients = nutrients
         notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflatedView = parent.inflate(R.layout.nutrients_view, false)
        return  ItemViewHolder(inflatedView)
    }

    //Will return the size of nutrients
    override fun getItemCount() = mNutrients.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
      //  holder.nutrientsDescription.text = mNutrients[position]?.description ?: ""
        holder.nutrientsDescription.text = mNutrients[position]?.description?.let {
            holder.context.getResources().getString(it)
        }
        holder.nutriensValue.text = mNutrients[position]?.value ?: ""
    }

    //The behavior of this class is tightly coupled with adapter, that's the reason why this is a nested class
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var nutrientsDescription = itemView.nutrientsDescription
        var nutriensValue = itemView.nutrientsValue
        val context = itemView.context
        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

        }

    }

}