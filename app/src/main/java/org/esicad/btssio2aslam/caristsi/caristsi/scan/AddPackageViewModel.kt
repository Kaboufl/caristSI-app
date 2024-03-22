package org.esicad.btssio2aslam.caristsi.caristsi.scan

import org.esicad.btssio2aslam.caristsi.caristsi.data.model.Package
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.esicad.btssio2aslam.caristsi.caristsi.R
import org.esicad.btssio2aslam.caristsi.caristsi.data.ApiClient
import org.esicad.btssio2aslam.caristsi.caristsi.data.PackageDataSource
import org.esicad.btssio2aslam.caristsi.caristsi.data.Result
import javax.inject.Inject

@HiltViewModel
class AddPackageViewModel @Inject constructor(
    private val dataSource: PackageDataSource
): ViewModel() {

    private val _newPackage = MutableLiveData<Package>()
    val newPackage: LiveData<Package> = _newPackage

    private val _addPackageResult = MutableLiveData<AddPackageResult>()
    val addPackageResult: LiveData<AddPackageResult> = _addPackageResult

    fun setNewPackage(`package`: Package) {
        _newPackage.value = `package`
    }

    fun addPackage(`package`: Package) {
        viewModelScope.launch {
            Log.i("AddPackageViewModel", "Ajout d'un colis")

            Log.i("AddPackageViewModel", "Colis : " + newPackage.value.toString())

            val result = dataSource.addPackage(`package`)

            if (result is Result.Success) {
                _addPackageResult.value =
                    AddPackageResult(success = true)
            } else {
                Log.e("AddPackageViewModel", "Echec de l'ajout du colis")
                Log.e("AddPackageViewModel", result.toString())
                _addPackageResult.value = AddPackageResult(error = R.string.post_package_failed)
            }



        }
    }
}