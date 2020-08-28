import { authenticate } from './../authentication.js';

import {
  displayErrorMessage,
  getRadioByName,
  isErrorMessage
} from './../miscellaneous.js';

import createListingInit from './create-listing.js';

import { suggestTags } from './tag-suggestions.js';

import updateListingInit from './update-listing.js';

import {
  checkFields,
  updateRemainingCharacterCount,
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
    let field = textFields[i];
    let fieldId = textFields[i].id;

    // Only add remaining character listener to non-location fields
    if (fieldId !== 'cause-location') {
      field.addEventListener('keyup', () => {
        checkFields();
        updateRemainingCharacterCount(fieldId, fieldId + '-remainder');
      });
      // Add remaining character onchange listener to tag field for when
      //    tag suggestions are added
      if (fieldId === 'cause-tags') {
        field.addEventListener('change', () => {
          checkFields();
          updateRemainingCharacterCount(fieldId, fieldId + '-remainder');
        });
      }
    } else {
      field.addEventListener('keyup', () => {
        checkFields();
      });
    }

    // Only add suggestion listener to non-cause fields
    if (fieldId !== 'cause-tags') {
      field.addEventListener('blur', () => {
        validateInput(fieldId, fieldId + '-instructions');
        suggestTags();
      });
    } else {
      field.addEventListener('blur', () => {
        validateInput(fieldId, fieldId + '-instructions');
      });
    }
  }

  let radioButtons = document.querySelectorAll('input[type="radio"]');

  // Call validateInput on radio changes
  for (let i = 0; i < radioButtons.length; i++) {
    radioButtons[i].addEventListener('change', () => {
      validateInput(radioButtons[i].id, 'cause-type-instructions');
      checkFields();
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
