import { fetchBlobstoreUrlAndSendData } from './../blobstore.js';

import { keyboardAccessibleOnClick } from './../miscellaneous.js';

import { appendParameterToFormData } from './new-listing.js';

import { createSendFormDataFunc } from './submit-listing.js';

/**
 * Initialize the update listing page.
 * Populate input elements with listings properties, and add functions to the 
 *     preview and submit button.
 */
export default function updateListingInit(listing, previewButton, submitButton) {
  const updateListing = createUpdateListingFunc(listing.key);
  populateInputWithListingInfo(listing);
  keyboardAccessibleOnClick(previewButton, updatePreview);
  keyboardAccessibleOnClick(submitButton, updateListing);
}

/**
 * This function will populate the input fields with information/values from 
 *     the listing that will be edited.
 *
 * @param listing JSON that represents a Listing
 */
function populateInputWithListingInfo(listing) {
  document.getElementById('cause-name').value = listing.name;
  document.getElementById(listing.type + '-radio').checked = true;
  document.getElementById('cause-location').value = listing.location;
  document.getElementById('cause-description').value = listing.description;
  document.getElementById('cause-how-to-help').value = listing.howToHelp;
  if (listing.website.length > 0) {
    document.getElementById('cause-website').value = listing.website;
  }
}

/**
 * Creates a function that will update the Listing.
 *
 * @param listingKey the key of the listing entity that will be updated.
 * @return a function that will update the current listing when called.
 */
function createUpdateListingFunc(listingKey) {
  let queryString = '/update-listing';

  const appendDataFunc = createAppendDataFunc(listingKey);
  const sendFormDataFunc = createSendFormDataFunc(appendDataFunc);

  return () => {
    fetchBlobstoreUrlAndSendData(queryString, sendFormDataFunc);
  }
}

/**
 * Creates a function that will take in a formData, append the listing key to 
 *     the formData, and append data from the new listing page to the formData.
 *
 * @param listingKey the key of the listing entity that will be updated.
 * @return a function that will append parameters to a formData.
 */
function createAppendDataFunc(listingKey) {
  return (formData) => {
    appendParameterToFormData(formData);
    
    formData.append('listing-key', listingKey);
  }
}

/**
 * Creates an update preview listing.
 */
function updatePreview() {
  let queryString = '/update-listing-preview';
  // create an update listing preview
}
