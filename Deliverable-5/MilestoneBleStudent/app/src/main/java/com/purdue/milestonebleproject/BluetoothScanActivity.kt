package com.purdue.milestonebleproject

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.purdue.milestonebleproject.adapter.ItemClickListener
import com.purdue.milestonebleproject.adapter.MainRecyclerViewAdapter
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityCompat

/**
 * BluetoothScanActivity
 * THIS IS FOR STUDENT USES. PLEASE REFER PROVIDED STEP-BY-STEP MANUAL TO COMPLETE THE CODE.
 *
 * @author Your Name (email Address)
 */

@SuppressLint("MissingPermission")
class BluetoothScanActivity : AppCompatActivity(), ItemClickListener {

    // Permission launcher
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("PermissionLauncher", "Permission Granted")
        } else {
            Toast.makeText(this, "Permission must be granted", Toast.LENGTH_LONG).show()
        }
    }

    // Required Permissions for BLUETOOTH
    val bluetoothPermissions = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN
    )

    // RecyclerView Adapter (DO NOT MODIFY THIS LINE)
    private val mRecyclerViewAdapter = MainRecyclerViewAdapter(this)

    // Main Bluetooth Adapter
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_scan)

        for (permission in bluetoothPermissions) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(permission)
            }
        }

        // get BluetoothLeScanner
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Unable to get BluetoothAdapter.", Toast.LENGTH_LONG).show()
            finish()    // Close Activity if there is no available Bluetooth Adapter.
        } else {
            bluetoothLeScanner = bluetoothAdapter!!.bluetoothLeScanner
        }

        // ----------------------------------------------------------------------------------------
        // DO NOT MODIFY CODE BELOW
        val mRecyclerView = findViewById<RecyclerView>(R.id.mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mRecyclerViewAdapter
        // ----------------------------------------------------------------------------------------
    }


    // Bluetooth Device Scanning
    // Variables for BLE Scanning
    private var scanning: Boolean = false                       // Scanning status
    private var bluetoothLeScanner: BluetoothLeScanner? = null  // Bluetooth LE scanner
    private val handler = Handler(Looper.getMainLooper())       // Handler object

    // Scan Callback object that BluetoothLeScanner would use.
    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.d("onScanFailed", "SCAN FAILED")
        }

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)

            // Add scanned device to a device list.
            if (result != null) {
                mRecyclerViewAdapter.bluetoothDevice.add(result.device)
                mRecyclerViewAdapter.notifyItemInserted(
                    mRecyclerViewAdapter.bluetoothDevice.size - 1
                )
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)

            results?.let {
                for (result in it) {
                    if (!mRecyclerViewAdapter.bluetoothDevice.contains(result.device)) {
                        mRecyclerViewAdapter.bluetoothDevice.add(result.device)
                        mRecyclerViewAdapter.notifyItemInserted(
                            mRecyclerViewAdapter.bluetoothDevice.size - 1
                        )
                    }
                }
            }
        }
    }

    // Handler Related
    private val SCAN_PERIOD = 10000L        // Scanning Timeout Period

    // Function for Start Scanning available BLE devices nearby.
    fun scanLeDevice() {
        if (scanning) {
            // Stop scanning after a pre-defined scanning timeout period.
            handler.postDelayed({
                scanning = false
                bluetoothLeScanner?.stopScan(leScanCallback)
            }, SCAN_PERIOD)

            bluetoothLeScanner?.startScan(leScanCallback)
        } else {
            bluetoothLeScanner?.stopScan(leScanCallback)
        }

    }



    // DO NOT MODIFY THE CODE BELOW HERE.
    // --------------------------------------------------------------------------------------------

    override fun onResume() {
        super.onResume()
        scanning = true
        scanLeDevice()
    }

    override fun onPause() {
        super.onPause()
        scanning = false
        scanLeDevice()
    }

    override fun onDestroy() {
        super.onDestroy()
        scanning = false
        scanLeDevice()
    }

    override fun onClick(v: View, position: Int) {
        // Get device name from Recycler view item
        val name = mRecyclerViewAdapter.bluetoothDevice[position].name ?: "Unknown Device"
        val address = mRecyclerViewAdapter.bluetoothDevice[position].address

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("DEVICE_NAME", name)
        intent.putExtra("DEVICE_ADDRESS", address)

        startActivity(intent)
    }

    // --------------------------------------------------------------------------------------------

}