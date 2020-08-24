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
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.users.UserService;
import com.google.sps.data.Listing;
import com.google.sps.utility.AuthenticationUtility;
import com.google.sps.utility.EntityUtility;
import com.google.sps.utility.ListingConstants;
import java.util.ArrayList;
import java.util.List;

/** A user */ 
public final class User {

  public static final String DELIMITER = " "; 

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
   * Used to transform a user Entity's String of listingkeys into a 
   *     List<Listing>.
   *
   * @param datastore the DatastoreService that connects to the back end.
   * @param entity An Entity that has a ListingKeys property
   * @param property the name of the ListingKeys property
   * @return a List<Listing>
   */
  private static List<Listing> getListings(DatastoreService datastore,   
      Entity entity, String property) throws Exception {
    String[] listingKeyStringArray = EntityUtility.getEntityKeyStrings(
        DELIMITER, entity, property);

    List<Listing> listings = new ArrayList<Listing>();
    if (listingKeyStringArray.length > 0) {
      listings = Listing.createListings(datastore, listingKeyStringArray);
    }

    return listings;
  }

  /**
   * Creates a User object from an Entity object that represents a user
   *
   * @param datastore the DatastoreService that connects to the back end.
   * @param entity the entity that represents a user
   * @return a User with all of the properties from the Entity
   */
  public static User createUser(DatastoreService datastore, Entity entity) 
      throws Exception {
    String bio = (String) entity.getProperty("bio");
    String email = (String) entity.getProperty("email");
    String username = (String) entity.getProperty("username");

    // Get a user's created listings.
    List<Entity> listingEntities = getCreatedListings(datastore, email);

    List<Listing> createdListings = Listing.createListings(listingEntities, email);
    List<Listing> upvotedListings = getListings(datastore, entity, 
        "upvotedListingKeys");

    return new User(bio, email, username, createdListings, 
        upvotedListings);
  }

  /**
   * Returns a User's created listing entities.
   *
   * @param datastore the DatastoreService that connects to the back end.
   * @param userEmail the email of the current user.
   * @return a User's created listing entities
   */
  public static List<Entity> getCreatedListings(DatastoreService datastore,
      String userEmail) {
    Filter filter = new FilterPredicate("ownersEmail", FilterOperator.EQUAL,
        userEmail);
    Query queryListing = new Query("Listing").setFilter(filter);
    PreparedQuery preparedQueryListings = datastore.prepare(queryListing);

    FetchOptions entitiesLimit = FetchOptions.Builder.withLimit(
        ListingConstants.LISTING_LIMIT);
    return preparedQueryListings.asList(entitiesLimit);
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
   * Add a specified listing key to the beginning of a userEntity property.
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
    //    or downvotedListingKeys
    String listingKeysString = getListingKeysAsString(userEntity, property);
    
    // Append a new listing key to the beginning of listingKeysString and update userEntity.
    listingKeysString = KeyFactory.keyToString(listingKey) + " " + 
        listingKeysString;
    userEntity.setProperty(property, listingKeysString);
    datastore.put(userEntity);
  }

  /**
   * Remove a specified listing key from a property in a userEntity.  
   *
   * @param datastore a datastore service instance
   * @param userEntity an entity representing a User
   * @param listingKey the key representing a specific Listing entity
   * @param property the property to remove the key from 
   */
  public static void removeListingKeyFromUserEntity(DatastoreService datastore,
      Entity userEntity, Key listingKey, String property) {
    // Get the string of listing keys from userEntity's specified property
    // This should most likely be createdListingKeys, upvotedListingKeys,
    //    or downvotedListingKeys
    String listingKeysString = getListingKeysAsString(userEntity, property);
    
    // Remove a specified key from listingKeysString and update userEntity
    listingKeysString = listingKeysString.replaceFirst(
        KeyFactory.keyToString(listingKey) + " ", "");
    userEntity.setProperty(property, listingKeysString);
    datastore.put(userEntity);
  }

  /**
   * Get an array of listing key strings from a property in a user entity.
   *
   * @param userEntity an entity associated with a specific user
   * @param property string of the property to get listing key strings from
   * @return String[] of listing key strings
   */
  public static String[] getListingKeysAsArray(Entity userEntity, String property,
      String delimiter) {
    String listingKeysString = (String) userEntity.getProperty(property);
    return listingKeysString.trim().split(delimiter);
  }

  /**
   * Get the string of listing key strings from a property in a user entity.
   *
   * @param userEntity an entity associated with a specific user
   * @param property string of the property to get listing key strings from
   * @return String of listing keys
   */
  public static String getListingKeysAsString(Entity userEntity, String property) {
    return (String) userEntity.getProperty(property);
  }

  /**
   * Adds a string of a key representing the current user entity into a
   * specified property of a listing and updates the listing entity.
   *
   * @param datastore a datastore service instance
   * @param userService a user service instance
   * @param listingKey the key of a listing in the datastore
   * @param property the property to add the current user's key to
   */
  public static void addCurrentUserKeyToListingEntity(DatastoreService datastore,
      UserService userService, Key listingKey, String property) throws Exception {

    Entity listingEntity = datastore.get(listingKey);
    String upvotedUsersString = (String) listingEntity.getProperty(property);

    Entity currentUserEntity =
        AuthenticationUtility.getCurrentUserEntity(datastore, userService);
    String currentUserKeyString = KeyFactory.keyToString(currentUserEntity.getKey());

    if (!upvotedUsersString.contains(currentUserKeyString)) {
      upvotedUsersString += currentUserKeyString + " ";
      listingEntity.setProperty(property, upvotedUsersString);
      datastore.put(listingEntity);
    }
  }

  /**
   * Removes a string of a key representing the current user entity from a
   * specified property of a listing and updates the listing entity.
   *
   * @param datastore a datastore service instance
   * @param userService a user service instance
   * @param listingKey the key of a listing in the datastore
   * @param property the property to remove the current user's key from
   */
  public static void removeCurrentUserKeyFromListingEntity(DatastoreService datastore,
      UserService userService, Key listingKey, String property) throws Exception {

    Entity listingEntity = datastore.get(listingKey);
    String upvotedUsersString = (String) listingEntity.getProperty(property);

    Entity currentUserEntity =
        AuthenticationUtility.getCurrentUserEntity(datastore, userService);
    String currentUserKeyString = KeyFactory.keyToString(currentUserEntity.getKey());

    if (upvotedUsersString.contains(currentUserKeyString)) {
      upvotedUsersString = upvotedUsersString.replaceFirst(currentUserKeyString + " ", "");
      listingEntity.setProperty(property, upvotedUsersString);
      datastore.put(listingEntity);
    }
  }
}
