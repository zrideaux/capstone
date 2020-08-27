import { authenticate } from './../authentication.js';

import {
  displayErrorMessage,
  getRadioByName,
  isErrorMessage
} from './../miscellaneous.js';

import { suggestTags } from './tag-suggestions.js';

import createListingInit from './create-listing.js';

import updateListingInit from './update-listing.js';

import {
  updateRemainingCharacters,
  validateInput
} from './validate-input.js';

window.onload = function() {
  authenticate(newListingInit);
  addEventListeners();
}

/**
 * Adds event listeners to each of the required fields on the form.
 */
function addEventListeners() {
  let textFields = document.querySelectorAll(
      'input:not([type="radio"]):not(#cause-website):not(#cause-image), textarea');

  for (let i = 0; i < textFields.length; i++) {
    let fieldId = textFields[i].id;

    // Only add remaining character listener to non-location fields
    if (textFields[i].id !== 'cause-location') {
      textFields[i].addEventListener('keyup', () => {
        updateRemainingCharacters(fieldId, fieldId + '-remainder');
      });
      // Add remaining character onchange listener to tag field
      if (fieldId === 'cause-tags') {
        textFields[i].addEventListener('onchange', () => {
          updateRemainingCharacters(fieldId, fieldId + '-remainder');
        });
      }
    }

    // Only add suggestion listener to non-cause fields
    if (fieldId !== 'cause-tags') {
      textFields[i].addEventListener('blur', () => {
        validateInput(fieldId, fieldId + '-instructions');
        suggestTags();
      });
    } else {
      textFields[i].addEventListener('blur', () => {
        validateInput(fieldId, fieldId + '-instructions');
      });
    }
  }

  let radioButtons = document.querySelectorAll('input[type="radio"]');

  // Call validateInput on radio changes
  for (let i = 0; i < radioButtons.length; i++) {
    radioButtons[i].addEventListener('onchange', () => {
      validateInput('fundraiser-radio', 'cause-type-instructions');
    });
  }
}

/**
 * Determines whether the user is creating or updating a listing.
 */
function newListingInit() {
  const searchParams = new URLSearchParams(document.location.search);

  const newListingTitle = document.getElementById('new-listing-title');

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
            updateListingInit(listing, newListingTitle, previewButton, submitButton);
          }
        });
  // The user is creating a new listing
  } else {
    createListingInit(newListingTitle, previewButton, submitButton);
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
  const tags = document.getElementById('cause-tags').value;
  const website = document.getElementById('cause-website').value;

  formData.append('name', name);
  formData.append('type', getRadioByName('cause-type'));
  formData.append('location', location);
  formData.append('description', description);
  formData.append('howToHelp', howToHelp);
  formData.append('tags', tags);
  formData.append('website', website);
}

export { appendParameterToFormData };
