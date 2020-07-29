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

/** A user for the website */ 
public final class Listing {

  private final String name;
  private final String type;
  private final String location;
  private final String howToHelp;
  private final String description;
  private final int upvotes;
  private final int downvotes;
  private final int views;
  private final long timestamp;

  public Listing(String bio, String email, String name, 
      String createdListingKeys, String upvotedListingKeys) {
    this.bio = bio;
    this.name = name;
    this.createdListingKeys = createdListingKeys;
    this.upvotedListingKeys = upvotedListingKeys;
  }
}
