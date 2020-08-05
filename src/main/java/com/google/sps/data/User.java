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
   * Create a new user entity and place it in the datastore.
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
}
