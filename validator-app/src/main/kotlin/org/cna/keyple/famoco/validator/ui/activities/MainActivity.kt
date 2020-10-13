/********************************************************************************
 * Copyright (c) 2020 Calypso Networks Association https://www.calypsonet-asso.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 ********************************************************************************/
package org.cna.keyple.famoco.validator.ui.activities

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.Toolbar
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import org.cna.keyple.famoco.validator.R
import org.cna.keyple.famoco.validator.ui.fragments.CardReaderFragment
import org.cna.keyple.famoco.validator.util.ActivityUtils.addFragmentToActivity

@VisibleForTesting
class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var mInjectedFragment: CardReaderFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup custom toolbar as main action bar
        val myToolbar =
            findViewById<Toolbar>(R.id.my_toolbar)
        myToolbar.title = ""
        setSupportActionBar(myToolbar)

        // Set up fragment
        val fragment = supportFragmentManager.findFragmentById(R.id.contentFrame) ?: mInjectedFragment
        addFragmentToActivity(supportFragmentManager, fragment, R.id.contentFrame)
    }
}
