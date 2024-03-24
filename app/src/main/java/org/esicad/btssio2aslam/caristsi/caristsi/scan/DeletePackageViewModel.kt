package org.esicad.btssio2aslam.caristsi.caristsi.scan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.esicad.btssio2aslam.caristsi.caristsi.R
import org.esicad.btssio2aslam.caristsi.caristsi.data.Result
import org.esicad.btssio2aslam.caristsi.caristsi.data.packages.PackageDataSource
import org.esicad.btssio2aslam.caristsi.caristsi.data.model.Package
import javax.inject.Inject

@HiltViewModel
class DeletePackageViewModel @Inject constructor(
    private val dataSource: PackageDataSource
): ViewModel() {

    private val _delPackage = MutableLiveData<Package>()
    val delPackage: LiveData<Package> = _delPackage

    private val _delPackageResult = MutableLiveData<DeletePackageResult>()
    val delPackageResult: LiveData<DeletePackageResult> = _delPackageResult

    fun setPackage(`package`: Package) {
        _delPackage.value = `package`
    }

    fun delete(`package`: Package) {
        viewModelScope.launch {
            Log.i("DeletePackageViewModel", "Suppression d'un colis")
            Log.i("DeletePackageViewModel", "Colis : " + delPackage.value.toString())

            val result = dataSource.deletePackage(`package`)

            if (result is Result.Success) {
                _delPackageResult.value =
                    DeletePackageResult(success = true)
            } else {
                Log.e("AddPackageViewModel", "Echec de la suppression du colis")
                Log.e("AddPackageViewModel", result.toString())
                _delPackageResult.value = DeletePackageResult(error = R.string.post_package_failed)
            }
        }
    }
}