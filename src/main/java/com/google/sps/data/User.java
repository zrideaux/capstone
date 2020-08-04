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
   * Creates a User object from an Entity object that represents a user
   *
   * @param entity the entity that represents a user
   * @return a User with all of the properties from the Entity
   */
  public static User createUser(Entity entity) {
    String bio = (String) entity.getProperty("bio");
    String email = (String) entity.getProperty("email");
    String username = (String) entity.getProperty("username");
    String createdListingKeys = (String) entity.getProperty("createdListingKeys");
    String upvotedListingKeys = (String) entity.getProperty("upvotedListingKeys");

    return new User(bio, email, username, createdListingKeys, 
        upvotedListingKeys);
  }  
}
