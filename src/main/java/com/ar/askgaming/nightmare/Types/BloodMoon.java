package com.ar.askgaming.nightmare.Types;

import com.ar.askgaming.nightmare.NightMare;

public class BloodMoon extends NightAbstract {

    public BloodMoon() {
        super(NightMare.Type.BLOOD_MOON);

        
        runTaskTimer(plugin, 20, 20);
    }

    @Override
    public void start() {


    }

    @Override
    public void end() {

        
    }
}
