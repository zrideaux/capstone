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
import com.google.sps.data.Listing;
import java.util.ArrayList;
import java.util.List;

/** A user */ 
public final class User {

  private final String bio;
  private final String email;
  private final String username;
  private final List<Listing> createdListings;
  private final List<Listing> upvotedListings;

  public User(String bio, String email, String username, 
      List<Listing> createdListings, List<Listing> upvotedListings) {
    this.bio = bio;
    this.email = email;
    this.username = username;
    this.createdListings = createdListings;
    this.upvotedListings = upvotedListings;
  }

  /**
   * Used to transform a String of listingkeys into a String[] of listingkeys
   *
   * @param datastore 
   * @param entity An Entity that has a ListingKeys property
   * @param property the name of the ListingKeys property
   * @return a String[] of listing keys
   */
  private static List<Listing> getListings(DatastoreService datastore,   
      Entity entity, String property) throws Exception {
    String listingKeysString = (String) entity.getProperty(property);
    List<Listing> listings = new ArrayList<Listing>();
    // If the user doesn't have any keys it is stored as " " a String of length 1. 
    // Thus, if the length of the String is greater than one that we have a key
    if (listingKeysString.length() > 1) {
      System.err.println("GET KEYS: " + listingKeysString);
      String[] listingKeyStringArray = listingKeysString.trim().split(" ");
      listings = Listing.createListingArray(datastore, listingKeyStringArray);
    }

    return listings;
  }

  /**
   * Used to transform a String of listingkeys into a String[] of listingkeys
   *
   * @param entity An Entity that has a ListingKeys property
   * @param property the name of the ListingKeys property
   * @return a String[] of listing keys
   */
  private static String[] getListingKeys(Entity entity, String property) {
    String listingKeysString = (String) entity.getProperty(property);

    return listingKeysString.trim().split(" ");
  }

  /**
   * Creates a User object from an Entity object that represents a user
   *
   * @param entity the entity that represents a user
   * @return a User with all of the properties from the Entity
   */
  public static User createUser(DatastoreService datastore, Entity entity) 
      throws Exception {
    String bio = (String) entity.getProperty("bio");
    String email = (String) entity.getProperty("email");
    String username = (String) entity.getProperty("username");
    List<Listing> createdListings = getListings(datastore, entity, 
        "createdListingKeys");
    List<Listing> upvotedListings = getListings(datastore, entity, 
        "upvotedListingKeys");

    return new User(bio, email, username, createdListings, 
        upvotedListings);
  }  

  /**
   * Create and return a new user entity.
   * 
   * @param request an http request to the servlet
   * @param userEmail the email to be associated with a the new user entity
   * @return An Entity who's email is userEmail
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
