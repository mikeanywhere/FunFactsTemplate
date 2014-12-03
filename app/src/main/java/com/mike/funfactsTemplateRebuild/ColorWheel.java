package com.mike.funfactsTemplateRebuild;

import android.graphics.Color;

import java.util.Random;

class ColorWheel {

    private int tapNumber = 0;

            private final String[] mColors = {
                    "#b7c0c7", "#ffd700", "#eec900", "#cdad00", "#8b7500", "#daa520", "#ffc125", "#eeb422", "#cd9b1d", "#8b6914", "#528b8b", "#00ced1", "#9400d3", "#ff1493", "#ee1289", "#cd1076", "#8b0a50", "#00bfff", "#00b2ee", "#009acd", "#00688b", "#696969", "#1e90ff", "#1c86ee", "#1874cd", "#104e8b", "#b22222", "#ff3030", "#ee2c2c", "#cd2626", "#8b1a1a", "#9bcd9b", "#698b69", "#483d8b", "#2f4f4f", "#556b2f", "#caff70", "#bcee68", "#a2cd5a", "#6e8b3d", "#ff8c00", "#ff7f00", "#ee7600", "#cd6600", "#8b4500", "#9932cc", "#bf3eff", "#b23aee", "#9a32cd", "#68228b", "#e9967a", "#8fbc8f", "#8b8878", "#00eeee", "#00cdcd", "#008b8b", "#b8860b", "#ffb90f", "#eead0e", "#cd950c", "#8b6508", "#006400", "#5f9ea0", "#98f5ff", "#8ee5ee", "#7ac5cd", "#53868b", "#7fff00", "#76ee00", "#66cd00", "#458b00", "#d2691e", "#ff7f24", "#ee7621", "#cd661d", "#ff7f50", "#ff7256", "#ee6a50", "#cd5b45", "#8b3e2f", "#6495ed", "#0000ff", "#0000ee", "#00008b", "#8a2be2", "#a52a2a", "#ff4040", "#ee3b3b", "#cd3333", "#8b2323", "#6b8e23", "#c0ff3e", "#b3ee3a", "#698b22", "#ffa500", "#ee9a00", "#cd8500", "#8b5a00", "#ff4500", "#ee4000", "#cd3700", "#8b2500", "#7d669e", "#51b46d", "#e0ab18", "#637a91", "#f092b0", "#39add1","#f9845b",
            };
    private final int[] mImages = {
      R.drawable.star_bang,R.drawable.background_a,R.drawable.background_b,R.drawable.background_c,R.drawable.background_d,R.drawable.background_e,R.drawable.background_f,R.drawable.background_g,R.drawable.background_h,R.drawable.background_i,R.drawable.background_j,R.drawable.background_k
    };
        public int getColor(){
            String color;
            Random randomGenerator = new Random();
            int randomNumber = randomGenerator.nextInt(mColors.length);
            color = mColors[randomNumber];
            int colorAsInt = Color.parseColor(color);
            return colorAsInt;
        }
        public int getBackgroundImage(){
            if(tapNumber < mImages.length) {
                int imageAsInt = mImages[tapNumber];
                tapNumber++;
                return imageAsInt;
            }else{
                tapNumber = 0;
                int imageAsInt = mImages[tapNumber];
                return imageAsInt;
            }


        }
}

