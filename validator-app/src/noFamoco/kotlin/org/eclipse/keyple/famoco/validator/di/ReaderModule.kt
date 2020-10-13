package org.eclipse.keyple.famoco.validator.di

import dagger.Module
import dagger.Provides
import org.eclipse.keyple.famoco.validator.di.scopes.AppScoped
import org.eclipse.keyple.famoco.validator.reader.IReaderRepository

/**
 *
 *  created on 18/09/2020
 *
 *  @author youssefamrani
 */

@Suppress("unused")
@Module
class ReaderModule {

    @Provides
    @AppScoped
    fun provideReaderRepository(): IReaderRepository = NoFamocoReaderRepositoryImpl()
}