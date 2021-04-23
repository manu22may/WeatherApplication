package com.capgemini.weatherapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.fragment_location.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LocationFragment : Fragment(){
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var loc:Location? = null
    private lateinit var lManager: LocationManager
    var longi=0.0
    var lati=0.0
    private var cityList = mutableListOf<String>("Chennai","Mumbai","Delhi","Bangalore","Pune")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerSetUp()

        checkPermissions()
        locationCurrentB.setOnClickListener {
            getlocation()
            longi= loc?.longitude?:0.0
            lati= loc?.latitude?:0.0
            val addr=getMyAddress(longi,lati)
            locationLattLongT.text = "LONGITUDE:${longi}\nLATTITUDE:${lati}\n$addr"
        }


        locationConfirmB.setOnClickListener {
            if(longi!=0.0 && lati!==0.0) {
                val pref = activity?.getSharedPreferences(
                    "location",
                    AppCompatActivity.MODE_PRIVATE
                )//creates file in internal memory
                val editor = pref?.edit()
                editor?.putString("longi", longi.toString())
                editor?.putString("lati", lati.toString())
                editor?.commit()
                Toast.makeText(activity,"Location Updated",Toast.LENGTH_LONG).show()
                activity?.onBackPressed()
            }
            else{
                Toast.makeText(activity,"Pls check location",Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun spinnerSetUp() {
         val adapter=ArrayAdapter<String>(activity!!,android.R.layout.simple_spinner_item,cityList)
        citiesSpinner.adapter=adapter
        citiesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position){
                    0->{
                        lati=13.067439
                        longi=80.237617
                    }
                    1->{
                        lati=19.076090
                        longi=72.877426
                    }
                    2->{
                        lati= 28.644800
                        longi=77.216721
                    }
                    3->{
                        lati=12.972442
                        longi=77.580643
                    }
                    4->{
                        lati=18.516726
                        longi=73.856255
                    }
                }
                val addr=getMyAddress(longi,lati)
                locationLattLongT.text = "LONGITUDE:${longi}\nLATTITUDE:${lati}\n$addr"
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }


    private fun getlocation() {
        activity?.apply {
            //----LOCATION MANAGER----
            lManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val providerList = lManager.getProviders(true)
            var providerName = ""

            //---GET PROVIDER---
            if (providerList.isNotEmpty()) {
                if (providerList.contains(LocationManager.GPS_PROVIDER)) {
                    providerName = LocationManager.GPS_PROVIDER
                } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
                    providerName = LocationManager.NETWORK_PROVIDER
                } else {
                    providerName = providerList[0]
                }


                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    finish()
                }

                loc = lManager.getLastKnownLocation(providerName)

            }
        }
    }


    private fun checkPermissions(){
        activity?.apply {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 1
                )
            }
        }
    }

    private fun getMyAddress(longi: Double, lati: Double): String {
        var addressList= mutableListOf<Address>()
        activity?.apply {
            val gCoder= Geocoder(this)
            addressList=gCoder.getFromLocation(
                lati,
                longi,1)
        }
        if(addressList.isNotEmpty()){
            val addr= addressList[0] //Address class
            return "${addr.locality} , ${addr.countryName}"
        }
        else
            return ""
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                LocationFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

}