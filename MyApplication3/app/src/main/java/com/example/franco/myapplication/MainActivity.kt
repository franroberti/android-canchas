package com.example.franco.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.json.JSONException
import org.json.JSONArray
import kotlinx.android.synthetic.main.activity_main.*
import android.os.AsyncTask
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.TextView
import java.io.IOException
import java.net.URL
import java.util.*
import android.app.DatePickerDialog
import android.view.View
import com.example.franco.myapplication.R.layout.activity_main
import android.view.ViewGroup
import android.view.LayoutInflater




class MainActivity : AppCompatActivity() {

    internal val CLUB_ID = "59b0ac5ab729bb0042bfcc8b"
    internal val API_URL = "https://canchas.ml/clubes/"
    var response: String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar3.progress = 0
        progressBar3.max = 100

        button.setOnClickListener { showDatePickerDialog(it) }

        if(response == null) {
            val searchTask = SearchTask()
            searchTask.execute(URL(API_URL + CLUB_ID))
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun showDatePickerDialog(view: View){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    button.text = "$year/$monthOfYear/$dayOfMonth"
                }, year, month, day)
        dialog.show()

    }



    inner class SearchTask : AsyncTask<URL, Void, String>() {

        // COMPLETED (2) Override the doInBackground metehod to perform the query. Return the results. (Hint: You've already written the code to perform the query)
        override fun doInBackground(vararg params: URL): String? {
            var searchResults: String? = null
            try {
                progressBar3.progress = 25
                searchResults = NetworkUtils().responseFromHttpUrl(params[0])
                progressBar3.progress = 50

            } catch (e: IOException) {
                e.printStackTrace()
            }

            response = searchResults

            return searchResults
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        override fun onPostExecute(searchResults: String?) {
            if (searchResults != null && searchResults != "") {

                val infoView = TextView(applicationContext)
                infoView.text = ""
                try {
                    val reservations = JSONArray(searchResults)
                    progressBar3.progress = 100

                    for (i in 0 until reservations.length()) {
                        var fieldName = reservations.getJSONObject(i).getString("nombre")

                        for (j in 0 until reservations.getJSONObject(i).getJSONArray("reservas").length()) {
                            var inflater = layoutInflater
                            var layoutR = R.layout.reserva
                            var view = inflater.inflate(layoutR, null)
                            var main = scrollContainer as ViewGroup
                            main.addView(view, 0)

                            infoView.append(" - desde:")
                            infoView.append(reservations.getJSONObject(i).getJSONArray("reservas").getJSONObject(i).getString("desde"))
                        }
                    }
                    scrollContainer.addView(infoView)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                progressBar3.visibility = 4
            }
        }

       fun parseResults(results: Array<Date>,dateFilter: Date): List<Date> {
           val reservations = results.filter { it.day == dateFilter.day}
           return reservations
       }
    }

}

