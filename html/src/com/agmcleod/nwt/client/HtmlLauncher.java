package com.agmcleod.nwt.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.agmcleod.nwt.CoreGame;

public class HtmlLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig () {
        return new GwtApplicationConfiguration(704, 704);
    }

    @Override
    public ApplicationListener getApplicationListener () {
        return new CoreGame();
    }
}