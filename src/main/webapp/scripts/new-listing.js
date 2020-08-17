import { authenticate } from './authentication.js';

import {
  fetchBlobstoreUrlAndSendData,
  sendFormData
} from './blobstore.js';

import { createListing } from './listing.js';

import { getRadioByName	} from './miscellaneous.js';

window.onload = function() {
  authenticate();
}

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
  formData.append('type', getRadioByName('cause-type'));
  formData.append('location', location);
  formData.append('description', description);
  formData.append('howToHelp', howToHelp);
  formData.append('website', website);
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

  window.location.href = '#preview-listings';
}

/**
 * Send new-listing form and input related data to the imageUploadURL and 
 *     create a preview with the response from the imageUploadURL servlet.
 * 
 * @param imageUploadURL the url to send listing data to.
 */
function sendPreviewListingFormData(imageUploadURL) {
  const newListingForm = document.getElementById('new-listing-form');
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

function validateInput(fieldId, instructionsId) {
  let field = document.getElementById(fieldId);
  let instructions = document.getElementById(instructionsId);

  if (field.checkValidity()) {
    // If field is valid
    if (field.classList.contains('invalid-input')) {
      field.classList.toggle('invalid-input');
      instructions.classList.toggle('invalid-input-instructions');
    }
    checkFields();
  } else {
    // If field is invalid
    if (!field.classList.contains('invalid-input')) {
      field.classList.toggle('invalid-input');
      instructions.classList.toggle('invalid-input-instructions');
    }
    disableSubmissions();
  }
}

function checkFields() {
  let fields = document.querySelectorAll('input, textarea');
  let submissionsAllowed = true;

  for (let i = 0; i < fields.length; i++) {
    if (fields[i].checkValidity() === false) {
      submissionsAllowed = false;
    }
  }

  if (submissionsAllowed) {
    enableSubmissions();
  }
}

function disableSubmissions() {
  let submissionButtons = document.getElementsByClassName('submission-buttons');
  for (let i = 0; i < submissionButtons.length; i++) {
    submissionButtons[i].setAttribute('disabled', '');
  }
  let stillNeededSpan = document.getElementById('still-needed');
  stillNeededSpan.style.visibility = "visible";
}

function enableSubmissions() {
  let submissionButtons = document.getElementsByClassName('submission-buttons');
  for (let i = 0; i < submissionButtons.length; i++) {
    submissionButtons[i].removeAttribute('disabled');
  }
  let stillNeededSpan = document.getElementById('still-needed');
  stillNeededSpan.style.visibility = "hidden";
}

export {
  validateInput,
  createNewListing,
  displayPreviewListing
};
