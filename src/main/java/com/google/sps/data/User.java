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

package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/** A user */ 
public final class User {

  private final String bio;
  private final String email;
  private final String username;
  private final String createdListingKeys;
  private final String upvotedListingKeys;

  public User(String bio, String email, String username, 
      String createdListingKeys, String upvotedListingKeys) {
    this.bio = bio;
    this.email = email;
    this.username = username;
    this.createdListingKeys = createdListingKeys;
    this.upvotedListingKeys = upvotedListingKeys;
  }

  /**
   * Create and return a new user entity.
   * 
   * @param request an http request to the servlet
   * @param userEmail the email to be associated with a the new user entity
   */
  public static Entity createUserEntity(String userEmail) {
    // Create User entity and add to datastore
    Entity newUserEntity = new Entity("User");
    newUserEntity.setProperty("email", userEmail);
    newUserEntity.setProperty("username", "");
    newUserEntity.setProperty("bio", "");
    newUserEntity.setProperty("createdListingKeys", " ");
    newUserEntity.setProperty("upvotedListingKeys", " ");
    newUserEntity.setProperty("downvotedListingKeys", " ");

    return newUserEntity;
  }

  /**
   * Add a specified listing key to a property in a userEntity.  
   *
   * @param datastore a datastore service instance
   * @param userEntity an entity representing a User
   * @param listingKey the key representing a specific Listing entity
   * @param property the property to append the key to 
   */
  public static void addListingKeyToUserEntity(DatastoreService datastore,
      Entity userEntity, Key listingKey, String property) {
    // Get the string of listing keys from userEntity's specified property
    // This should most likely be createdListingKeys, upvotedListingKeys,
    // or downvotedListingKeys
    String listingKeysString = (String) userEntity.getProperty(property);
    
    // Append a new listing key to the end of listingKeysString and update userEntity
    listingKeysString += KeyFactory.keyToString(listingKey) + " ";
    userEntity.setProperty(property, listingKeysString);
    datastore.put(userEntity);
  }
}
