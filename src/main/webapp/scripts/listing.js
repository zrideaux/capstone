import { 
  createDivElement, 
  createPElement,
  createSectionElement
} from './htmlElement.js';

import createListingPreview from './listing-preview.js';

import createListingDetailedView from './listing-detailed.js';

import { 
  isErrorMessage,
  displayErrorMessage
} from './miscellaneous.js';

/**
 * Creates a div that contains listings
 *
 * @param containerElement the element that append the listings to
 * @param listingsClass the class attribute for the listings div
 * @param listingsId the id attribute for the listings div
 * @param queryString the String that represents the query to a servlet that 
 *     returns a List of Listings
 */
function getListings(containerElement, listingsClass, listingsId,
    queryString, responseJsonFunc = getListingsResponseJson) {
  // Fetching user listings data
  fetch(queryString)
      .then(response => response.json())
      .then((listingsArray) => {
        if (isErrorMessage(listingsArray)) {
          displayErrorMessage(listingsArray);
        } else {
          responseJsonFunc(containerElement, listingsArray, 
              listingsClass, listingsId);
        }
      });
}

/**
 * Appends listings to a containerElement.
 * Made to be used in the getListings function
 *
 * @param containerElement the element that contains listings
 * @param listingsArray a JSON array of JSON that represents listings
 * @param listingsClass the class attribute for the listings div
 * @param listingsId the id attribute for the listings div
 */
function getListingsResponseJson(containerElement, listingsArray, listingsClass,
    listingsId) {
  containerElement.appendChild(
      createListings(listingsArray, listingsClass, listingsId));
}

/**
 * Creates a div with a user's listings.
 *
 * @param listings an array of json that represents a listing
 * @param listingsId the id of this element
 * @param listingsClass the class of this element
 * @return a div with all of a user's listings.
 */
function createListings(listings, listingsClass, listingsId) {
  const divListings = createDivElement('', listingsClass, listingsId);
  const numListings = listings.length;
  if (numListings > 0) {
    for (let i = 0; i < numListings; i ++) {
      const listing = listings[i];
      const id = listingsId + (i + 1);
      divListings.appendChild(createListing(id, listing));
    }
  } else {
    divListings.appendChild(createPElement('No listings', '', ''));
  }

  return divListings;
}

/**
 * Creates a section with a preview and a detailed listing
 * @param cardElementId the id of the section element containing the prewiew 
 *     and detailed listing
 * @param listing the json with listing information
 * @return a section that represents a listing
 */
function createListing(cardElementId, listing) {
  const sectionListing = createSectionElement('', '');

  const cardElementDisplay = "flex";

  // Create listing preview
  sectionListing.appendChild(createListingPreview(listing, cardElementDisplay, 
      cardElementId));
    
  // Create listing view (detiled view)
  sectionListing.appendChild(createListingDetailedView(listing, 
      cardElementDisplay, cardElementId));

  return sectionListing;
}

export {
  createListing,
  createListings,
  getListings,
  getListingsResponseJson
};
