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
package org.cna.keyple.famoco.validator.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.cna.keyple.famoco.validator.di.scopes.ActivityScoped
import org.cna.keyple.famoco.validator.di.scopes.FragmentScoped
import org.cna.keyple.famoco.validator.ui.activities.MainActivity
import org.cna.keyple.famoco.validator.ui.fragments.CardReaderFragment

@Module
abstract class UIModule {
    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun cardReaderFragment(): CardReaderFragment
}
