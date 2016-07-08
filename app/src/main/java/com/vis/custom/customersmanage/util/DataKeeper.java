/*
 * Copyright (C) 2013 litesuits.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.vis.custom.customersmanage.util;

import android.content.Context;
import android.content.SharedPreferences;



public class DataKeeper {
    private SharedPreferences sp;
    public static final  String NAME = "config";


    public DataKeeper(Context context, String fileName) {
        sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }
    public void put(String key, String value) {
        if (value == null) {
            sp.edit().remove(key).commit();
        } else {
            sp.edit().putString(key, value).commit();
        }
    }



    public String get(String key, String defValue) {
        return sp.getString(key, defValue);
    }

}
