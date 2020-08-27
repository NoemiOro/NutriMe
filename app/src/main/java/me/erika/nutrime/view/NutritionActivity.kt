package me.erika.nutrime.view

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_nutrition.*
import me.erika.nutrime.R
import me.erika.nutrime.adapter.NutritionAdapter
import me.erika.nutrime.repository.NutrientsDTO
import me.erika.nutrime.utilities.NutritionDialog
import me.erika.nutrime.viewModel.NutritionActivityViewModel


class NutritionActivity : AppCompatActivity() {

    lateinit var mViewModel: NutritionActivityViewModel
    lateinit var nutritionAdapter: NutritionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutrition)
        setUpViewModel()
        setUpUI()
        setUpObservers()
    }

    private fun setUpViewModel() {
        mViewModel = ViewModelProvider(this).get(NutritionActivityViewModel::class.java)
    }

    private fun setUpUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nutrition_nutrients_recycler.layoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL,
            false
        )
        nutritionAdapter = NutritionAdapter()
        nutrition_nutrients_recycler.adapter = nutritionAdapter

        setUpSearch()
    }

    private fun setUpObservers() {
        mViewModel.nutrientsLiveData.observe(this, getNutrientsObserver)
        mViewModel.otherNutritionInfo.observe(this, getOtherNutritionObserver)
        mViewModel.errorMessage.observe(this, errorMessageObserver)
    }

    private val getNutrientsObserver = Observer<ArrayList<NutrientsDTO?>> {
        if(it != null && it.size > 0) {
            nutritionAdapter.setNutritionFacts(it)
            nutrition_result_layout.visibility = View.VISIBLE
        }
        else{
            nutrition_search_layout.visibility = View.VISIBLE
            nutrition_result_layout.visibility = View.GONE
        }
        nutrition_nutrients_pb.visibility = View.GONE
    }

    private val getOtherNutritionObserver = Observer<Map<Int,String>> {
        nutrition_label_tv.text = it.get(R.string.label)?.capitalize() ?: ""
        nutrition_label_category.text = it.get(R.string.category)?.capitalize() ?: ""

        val imageUri = it.get(R.string.image)
        Picasso.get().load(imageUri).into(nutrition_food_iv)
    }

    private val errorMessageObserver = Observer<Int> {
        NutritionDialog().simpleAlert(this, resources.getString(it))
    }

    fun setUpSearch(){

        nutrition_search_et.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ) {
                nutrition_result_layout.visibility = View.GONE
                nutrition_nutrients_pb.visibility = View.VISIBLE
                mViewModel.getFood(v.text.toString())
                nutrition_search_et.text.clear()

                return@OnEditorActionListener false
            }
            false
        })
    }
}



//To call fragment
//            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
//            transaction.add(R.id.container, ListOfItemsFragment())
//            transaction.commit()