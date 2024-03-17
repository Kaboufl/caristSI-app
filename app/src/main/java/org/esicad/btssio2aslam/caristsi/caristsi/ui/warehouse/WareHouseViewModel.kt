package org.esicad.btssio2aslam.caristsi.caristsi.ui.warehouse


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.esicad.btssio2aslam.caristsi.caristsi.data.ApiClient
import org.esicad.btssio2aslam.caristsi.caristsi.data.model.Package
import javax.inject.Inject

/**
 * La classe WareHouseViewModel représente une délégation
 * (utilisation du design pattern [Façade](https://refactoring.guru/fr/design-patterns/facade)
 * du chargement des données relatives à l'entrepôt (la liste des objets Package)
 */
@HiltViewModel
class WareHouseViewModel @Inject constructor(private val api: ApiClient) : ViewModel() {
    /**
     * MutableLiveData qui stocke les données
     * propriété privée car mutable; seule `loadPackages` a le droit de modifier les données
     * pour accéder à la donnée dans les autres parties de l'app, on va utiliser `packagesState`
     */
    private val _packages = MutableLiveData<MutableList<Package>>()
    val packagesState: LiveData<MutableList<Package>> = _packages

    /**
     * Cette fonction va déclencher le chargement de la donnée dans un contexte particulier
     * (afin de ne pas impacter le processus principal de l'application (notion de programmation concurrente)
     */
    fun loadPackages() {
        // on lance le chargement (opération asynchrone et bloquante) dans un scope (un contexte) fourni par android
        viewModelScope.launch {
            val list = mutableListOf<Package>()
            list.addAll(api.packageService.getPackages())
            _packages.value = list
        }
    }
}