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
                try {
                    val reservations = JSONArray(searchResults)
                    progressBar3.progress = 100

                    for (i in 0 until reservations.length()) {
                        var reservation = reservations.getJSONObject(i)
                        var fieldName = reservation.getString("nombre")
                        var reservas = reservation.getJSONArray("reservas")
                        for (j in 0 until reservas.length()) {
                            var textToAppend = TextView(applicationContext)
                            textToAppend.text = fieldName + " - "

                            textToAppend.append(Date(reservas.getJSONObject(i).getString("desde").toLong()).hours.toString())
                            textToAppend.append(" hs")

                            scrollContainer.addView(textToAppend)
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                progressBar3.visibility = 4
            }
        }

       fun JSONArray.filterDateResults(filterDate: Date): List<Date> {
           var list = emptyList<Date>()
           for (j in 0 until this.length()) {
               if(compareDateDay(Date(this.getJSONObject(j).getString("desde").toLong()),filterDate)){
                   list += (Date(this.getJSONObject(j).getString("desde").toLong()))
               }
           }
           return list
       }

        fun compareDateDay(date1: Date,date2: Date):Boolean =
                ((date1.year)==(date2.year) && (date1.month)==(date2.month) && (date1.day)==(date2.day))
    }

}

