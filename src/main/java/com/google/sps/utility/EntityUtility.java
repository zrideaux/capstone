// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.utility;  

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.ArrayList;
import java.util.List;
  
public final class EntityUtility {  
  /**
   * Turns a String[] of Entity key Strings into a List<Entity>.
   *
   * @param datastore the DatastoreService that connects to the back end.
   * @param entityKeysStringArray the String[] of Entity key
   *     Strings that will each be used to create an Entity.
   * @return List<Entity>
   */
  public static List<Entity> createEntities(DatastoreService datastore, 
      String[] entityKeysStringArray) throws Exception {
    List<Entity> entities = new ArrayList<Entity>();
    for (String entityKeyString : entityKeysStringArray) {
      Key entityKey = KeyFactory.stringToKey(entityKeyString);
      Entity entity = datastore.get(entityKey);
      entities.add(entity);
    }

    return entities;
  }
  
  /**
   * Used to transform an Entity's String of Entity keys into a 
   *     List<Entity>.
   *
   * @param delimiter the used to separate the String of Entity keys 
   * @param datastore the DatastoreService that connects to the back end.
   * @param entity An Entity that has a property that stores Entity keys.
   * @param property the name of the property that stores Entity keys.
   * @return a String[] of Entity keys.
   */
  public static List<Entity> getEntities(String delimiter, 
      DatastoreService datastore, Entity entity, String property) 
      throws Exception {
    String[] entityKeyStringArray = getEntityKeyStrings(delimiter, entity, 
        property);

    List<Entity> entities = new ArrayList<Entity>();
    if (entityKeyStringArray.length > 0) {
      entities = createEntities(datastore, entityKeyStringArray);
    }

    return entities;
  }

  /**
   * Used to transform an Entity's String of Entity keys into a 
   *     List<Entity>.
   *
   * @param delimiter the used to separate the String of Entity keys 
   * @param entity An Entity that has a property that stores Entity keys.
   * @param property the name of the property that stores Entity keys.
   * @return a String[] of Entity keys.
   */
  public static String[] getEntityKeyStrings(String delimiter, Entity entity, String property) 
      throws Exception {
    String entityKeysString = (String) entity.getProperty(property);
    String[] entityKeyStringArray = new String[0];
    // If the user doesn't have any keys it is stored as " " a String of length 1. 
    // If the length of the String is greater than one, we have key(s)
    if (entityKeysString.length() > 1) {
      entityKeyStringArray = entityKeysString.trim().split(delimiter);
    }

    return entityKeyStringArray;
  }
}