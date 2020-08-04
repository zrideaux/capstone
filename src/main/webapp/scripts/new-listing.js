import {
  fetchBlobstoreUrlAndSendData,
  sendFormData
} from './blobstore.js';

import { createListing } from './listing.js';

/**
 * Creates a string of parameters to query a servlet.
 * 
 * @param formData the formData to append parameters to that will be sent to 
 *     servlet.
 */
function appendParameterToFormData(formData) {
  const name = document.getElementById('cause-name').value;
  const location = document.getElementById('cause-location').value;
  const description = document.getElementById('cause-description').value;
  const howToHelp = document.getElementById('cause-how-to-help').value;
  const website = document.getElementById('cause-website').value;
  formData.append('name', name);
  formData.append('type', getType());
  formData.append('location', location);
  formData.append('description', description);
  formData.append('howToHelp', howToHelp);
  formData.append('website', website);
}

/**
 * Gets the type of the the listing chosen in the form.
 *
 * @return the value of the radio type or an empty sting if none have been 
 *     chosen.
 */
function getType() {
  const radioTypes = document.getElementsByName("cause-type");
  for (let i = 0; i < radioTypes.length; i++) {
    const radioType = radioTypes[i];
    if (radioType.checked) {
      return radioType.value;
    }
  }
  return "";
}

// The following functions are used to create a preview of the new listing

/**
 * Creates a listing preview for the user to see.
 */
function displayPreviewListing() {
  const containerElement = document.getElementById("preview");
  let queryString = '/create-listing-preview';
  containerElement.innerHTML = '';

  fetchBlobstoreUrlAndSendData(queryString, sendPreviewListingFormData);
}

/**
 * Send new-listing form and input related data to the imageUploadURL and 
 *     create a preview with the response from the imageUploadURL servlet.
 * 
 * @param imageUploadURL the url to send listing data to.
 */
function sendPreviewListingFormData(imageUploadURL) {
  const newListingForm = document.getElementById("new-listing-form");
  sendFormData(appendParameterToFormData, newListingForm, imageUploadURL, 
      createPreviewListing);
}

/**
 * Creates a listing given a Listing JSON and appends the listing the the 
 *     preview container element in the newlisting page.
 *
 * @param listing JSON that represents a Listing object.
 */
function createPreviewListing(listing) {
  const containerElement = document.getElementById("preview");
  containerElement.appendChild(
      createListing('preview-listings', listing));
}

// The following functions are used to create a new listing
/**
 * Creates a listing preview for the user to see.
 */
function createNewListing() {
  let queryString = '/create-listing';
  fetchBlobstoreUrlAndSendData(queryString, sendNewListingFormData);
}

/**
 * Send new-listing form and input related data to the imageUploadURL to create 
 *     a new listing and then redirect the user to the user page.
 * 
 * @param imageUploadURL the url to send listing data to.
 */
function sendNewListingFormData(imageUploadURL) {
  const newListingForm = document.getElementById("new-listing-form");
  sendFormData(appendParameterToFormData, newListingForm, imageUploadURL, 
      redirectToUserPage);
}

/**
 * Redirect the user to the user page.
 */
function redirectToUserPage() {
  location.replace('user.html');
}

export {
  createNewListing,
  displayPreviewListing
};
