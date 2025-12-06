/*
 * Copyright 2000-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jetbrains.micropython.settings

import com.intellij.facet.FacetConfiguration
import com.intellij.facet.ui.FacetEditorContext
import com.intellij.facet.ui.FacetEditorTab
import com.intellij.facet.ui.FacetEditorValidator
import com.intellij.facet.ui.FacetValidatorsManager
import com.intellij.openapi.components.BaseState
import com.intellij.util.xmlb.XmlSerializerUtil
import com.jetbrains.micropython.devices.MicroPythonDeviceProvider

/**
 * @author vlan
 */
class MicroPythonFacetConfiguration : FacetConfiguration, BaseState() {
  var deviceProviderName by string(MicroPythonDeviceProvider.default.persistentName)
  
  var deviceProvider: MicroPythonDeviceProvider
    get() = MicroPythonDeviceProvider.providers.firstOrNull { it.persistentName == deviceProviderName }
        ?: MicroPythonDeviceProvider.default
    set(value) {
      deviceProviderName = value.persistentName
    }

  override fun createEditorTabs(editorContext: FacetEditorContext, validatorsManager: FacetValidatorsManager): Array<FacetEditorTab> {
    val facet = editorContext.facet as MicroPythonFacet
    validatorsManager.registerValidator(object: FacetEditorValidator() {
      override fun check() = facet.checkValid()
    })
    return arrayOf(MicroPythonFacetEditorTab(this, facet))
  }
}
