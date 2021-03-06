import { fetchBlobstoreUrlAndSendData } from './../blobstore.js';

import {
  createButtonElement,
  createDivElement,
  createHElement
} from './../htmlElement.js';

import { createListing } from './../listing.js';

import { keyboardAccessibleOnClick } from './../miscellaneous.js';

import { appendParameterToFormData } from './new-listing.js';

import {
  createCreatePreviewListingFunc,
  createPreviewSendFormDataFunc,
  displayPreviewListing
} from './preview-listing.js';

import { createSubmitSendFormDataFunc } from './submit-listing.js';

import {
  checkFields,
  validateInput,
  updateRemainingCharacterCount
} from './validate-input.js';

/**
 * Initialize the update listing page.
 * Populate input elements with listings properties, and add functions to the 
 *     preview and submit button.
 *
 * @param listing JSON the represents a listing.
 * @param newListingTitle an element that represents the new lising title.
 * @param previewButton an element that represents the preview button.
 * @param submitButton an element that represents a submit button.
 */
export default function updateListingInit(listing, newListingTitle, 
    previewButton, submitButton) {
  newListingTitle.innerHTML = 'Update Listing';
  const updatePreview = createPreviewListingFunc(listing.key);
  const updateListing = createUpdateListingFunc(listing.key);
  populateInputWithListingInfo(listing);
  keyboardAccessibleOnClick(previewButton, updatePreview);
  keyboardAccessibleOnClick(submitButton, updateListing);
  addUndoOptionToFields(listing);
  setRemainingCharacterCount();
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
  document.getElementById('cause-tags').value = listing.tags;
  if (listing.website.length > 0) {
    document.getElementById('cause-website').value = listing.website;
  }
}

/**
 * This function adds a button that reverts an edited field to its original
 * value.
 *
 * @param listing JSON of a listing's properties
 */
function addUndoOptionToFields(listing) {
  let inputFields = document.querySelectorAll(
      '#cause-name, #cause-location, #cause-description, \
      #cause-how-to-help, #cause-tags');

  let originalValues = {
    'cause-name': listing.name,
    'cause-location': listing.location,
    'cause-description': listing.description,
    'cause-how-to-help': listing.howToHelp,
    'cause-tags': listing.tags
  };

  for (let i = 0; i < inputFields.length; i++) {
    let undoButton = createButtonElement('Undo Changes', 'undo-button', '');
    let fieldId = inputFields[i].id;

    if (fieldId === 'cause-location') {
      undoButton.onclick = () => {
        document.getElementById(fieldId).value = originalValues[fieldId];
        validateInput(fieldId, fieldId + '-instructions');
        checkFields();
      };
    } else {
      undoButton.onclick = () => {
        document.getElementById(fieldId).value = originalValues[fieldId];
        updateRemainingCharacterCount(fieldId, fieldId + '-remainder');
        validateInput(fieldId, fieldId + '-instructions');
        checkFields();
      };
    }

    document.getElementById(fieldId + '-actions').appendChild(undoButton);
  }
}

/**
 * Calls function to set initial remaining character counts on each of the
 * fields with max lengths when a listing is being updated.
 */
function setRemainingCharacterCount() {
  let inputFields = document.querySelectorAll(
      '#cause-name, #cause-description, \
      #cause-how-to-help, #cause-tags');

  for (let i = 0; i < inputFields.length; i++) {
    let fieldId = inputFields[i].id;
    updateRemainingCharacterCount(fieldId, fieldId + '-remainder');
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
  const createPreviewListingFunc = createCreatePreviewListingFunc(
      updatedPreviewListingFunc);

  const sendFormDataFunc = createPreviewSendFormDataFunc(appendFormDataFunc, createPreviewListingFunc);
  // create an update listing preview
  return () => {
    displayPreviewListing(queryString, sendFormDataFunc);
  }
}

/**
 * Creates two updated preview listings from the response.
 * One that is the original listng and another which is the udpated listing.
 *
 * @param containerElement an element to add the Listings too.
 * @param response a JSON that represents a list of Listings.
 */
function updatedPreviewListingFunc(containerElement, response) {
  containerElement.appendChild(
      createUpdatedPreviewListing(response[0], 'Original Listing'));
  
  containerElement.appendChild(
      createUpdatedPreviewListing(response[1], 'Updated Listing'));
}

/**
 * Creates an updated preview listing, which is a preview listing with a title.
 *
 * @param listing a JSON that represents a listing.
 * @param title a String to use as the title of this preview listing.
 * @return an updated preview listing.
 */
function createUpdatedPreviewListing(listing, title) {
  const previewContainerDiv = createDivElement('', 'updated-preview-container', 
      '');

  previewContainerDiv.appendChild(createHElement(title, '2', '', ''));

  previewContainerDiv.appendChild(
      createListing('preview-listings', listing));

  return previewContainerDiv;
}
