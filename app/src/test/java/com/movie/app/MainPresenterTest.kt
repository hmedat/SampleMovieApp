package com.movie.app

import com.movie.app.api.ApiInterface
import com.movie.app.main.MainActivityContractor
import com.movie.app.main.MainPresenter
import com.movie.app.util.schedulers.SchedulerProvider
import org.junit.Before
import org.mockito.Mock

class MainPresenterTest {


    @Mock
    private lateinit var schedulerProvider: SchedulerProvider
    @Mock
    private lateinit var view: MainActivityContractor.View
    @Mock
    private lateinit var apiInterface: ApiInterface

    lateinit var presenter: MainPresenter

    @Before
    @Throws(Exception::class)
    fun setUp() {
        presenter = MainPresenter(schedulerProvider, apiInterface, view)
    }
}
