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
import java.util.Collection;
import java.util.List;
  
public final class EntityUtility {  
  /**
   * Used to transform an Entity's String of Entity keys into  
   *     Entities that can be added to a Collection of Entities.
   *
   * @param datastore the DatastoreService that connects to the back end.
   * @param delimiter the used to separate the String of Entity keys 
   * @param entity An Entity that has a property that stores Entity keys.
   * @param entityCollection A collection of Entities.
   * @param property the name of the property that stores Entity keys.
   */
  public static void addEntities(DatastoreService datastore, String delimiter, 
      Entity entity, Collection entityCollection, String property) 
      throws Exception {
    String[] entityKeyStringsArray = getEntityKeyStrings(delimiter, entity, 
        property);

    if (entityKeyStringsArray.length > 0) {
      for (String entityKeyString : entityKeyStringsArray) {
        Key newEntityKey = KeyFactory.stringToKey(entityKeyString);
        Entity newEntity = datastore.get(newEntityKey);

        entityCollection.add(newEntity);
      }
    }
  }

  /**
   * Used to transform an Entity's String of Entity keys into  
   *     Entity Key Strings that can invdividually be added to a Collection of 
   *     Entities.
   *
   * @param datastore the DatastoreService that connects to the back end.
   * @param delimiter the used to separate the String of Entity keys 
   * @param entity An Entity that has a property that stores Entity keys.
   * @param entityCollection A collection of Entities.
   * @param property the name of the property that stores Entity keys.
   */
  public static void addEntityKeyStrings(DatastoreService datastore, 
      String delimiter, Entity entity, Collection entityCollection, 
      String property) throws Exception {
    String[] entityKeyStringsArray = getEntityKeyStrings(delimiter, entity, 
        property);

    if (entityKeyStringsArray.length > 0) {
      for (String entityKeyString : entityKeyStringsArray) {
        entityCollection.add(entityKeyString);
      }
    }
  }

  /**
   * Turns a String[] of Entity key Strings into a List<Entity>.
   *
   * @param datastore the DatastoreService that connects to the back end.
   * @param entityKeyStringsArray the String[] of Entity key
   *     Strings that will each be used to create an Entity.
   * @return List<Entity>
   */
  public static List<Entity> createEntities(DatastoreService datastore, 
      String[] entityKeyStringsArray) throws Exception {
    List<Entity> entities = new ArrayList<Entity>();
    for (String entityKeyString : entityKeyStringsArray) {
      entities.add(getEntityFromKey(datastore, entityKeyString));
    }

    return entities;
  }

  /**
   * Turns an Entity key String into an Entity.
   *
   * @param datastore the DatastoreService that connects to the back end.
   * @param entityKeyString the Entity key String used to create an Entity.
   * @return Entity
   */
  public static Entity getEntityFromKey(DatastoreService datastore, 
      String entityKeyString) throws Exception {
    Key entityKey = KeyFactory.stringToKey(entityKeyString);
    Entity entity = datastore.get(entityKey);
    
    return entity;
  }
  
  /**
   * Used to transform an Entity's String of Entity keys into a 
   *     List<Entity>.
   *
   * @param datastore the DatastoreService that connects to the back end.
   * @param delimiter the used to separate the String of Entity keys 
   * @param entity An Entity that has a property that stores Entity keys.
   * @param property the name of the property that stores Entity keys.
   * @return a String[] of Entity keys.
   */
  public static List<Entity> getEntitiesFromProperty(DatastoreService datastore,
      String delimiter, Entity entity, String property) 
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

  /**
   * Updates an Entities property value if the stringPropertyValue's length is 
   *     greater than 0 and it is not the same as the current property value.
   *
   * @param entity the entity to update.
   * @param propertyName the name of the property to update.
   * @param stringPropertyValue the new property value to update to.
   */
  public static void updateStringProperty(Entity entity, String propertyName, 
      String stringPropertyValue) {
    if (stringPropertyValue.length() > 0) {
      String currentStringValue = (String) entity.getProperty(propertyName);
      if (!stringPropertyValue.equals(currentStringValue)) {
        entity.setProperty(propertyName, stringPropertyValue);
      }
    }
  }  
}
