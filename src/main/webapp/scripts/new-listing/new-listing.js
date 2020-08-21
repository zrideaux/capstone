import { authenticate } from './../authentication.js';

import {
  displayErrorMessage,
  getRadioByName,
  isErrorMessage
} from './../miscellaneous.js';

import createListingInit from './create-listing.js';

import updateListingInit from './update-listing.js';

window.onload = function() {
  authenticate(newListingInit);
}

/**
 * Determines whether the user is creating or updating a listing.
 */
function newListingInit() {
  const searchParams = new URLSearchParams(document.location.search);

  const previewButton = document.getElementById('create-preview-button');
  const submitButton = document.getElementById('create-listing-button');

  // The user is updating a listing 
  if (searchParams.has('key')) {
    const query = '/fetch-listing?listing-key=' + searchParams.get('key');

    fetch(query)
        .then(response => response.json())
        .then((listing) => {
          if(isErrorMessage(listing)) {
            displayErrorMessage(listing);
          } else {
            updateListingInit(listing, previewButton, submitButton);
          }
        })
  // The user is creating a new listing
  } else {
    createListingInit(previewButton, submitButton);
  }
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

export { appendParameterToFormData };
