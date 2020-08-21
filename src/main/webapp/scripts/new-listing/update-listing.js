import { fetchBlobstoreUrlAndSendData } from './../blobstore.js';

import { createListing } from './../listing.js';

import { keyboardAccessibleOnClick } from './../miscellaneous.js';

import { appendParameterToFormData } from './new-listing.js';

import {
  createCreatePreviewListingFunc,
  createPreviewSendFormDataFunc,
  displayPreviewListing
} from './preview-listing.js';

import { createSubmitSendFormDataFunc } from './submit-listing.js';

/**
 * Initialize the update listing page.
 * Populate input elements with listings properties, and add functions to the 
 *     preview and submit button.
 */
export default function updateListingInit(listing, newListingTitle, 
    previewButton, submitButton) {
  newListingTitle.innerHTML = 'Update Listing';
  const updatePreview = createPreviewListingFunc(listing.key);
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

  const appendDataFunc = createAppendFromDataFunc(listingKey);
  const sendFormDataFunc = createSubmitSendFormDataFunc(appendDataFunc);

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
function createAppendFromDataFunc(listingKey) {
  return (formData) => {
    appendParameterToFormData(formData);
    
    formData.append('listing-key', listingKey);
  }
}

/**
 * Creates an update preview listing.
 */
function createPreviewListingFunc(listingKey) {
  let queryString = '/update-listing-preview';

  const appendFormDataFunc = createAppendFromDataFunc(listingKey);
  const createPreviewListingFunc = createCreatePreviewListingFunc(updatedPreviewListingFunc);

  const sendFormDataFunc = createPreviewSendFormDataFunc(appendFormDataFunc, createPreviewListingFunc);
  // create an update listing preview
  return () => {
    displayPreviewListing(queryString, sendFormDataFunc);
  }
}

function updatedPreviewListingFunc(containerElement, response) {
  containerElement.appendChild(
      createListing('preview-listings', response[0]));
  
  containerElement.appendChild(
      createListing('preview-listings', response[1]));
}
