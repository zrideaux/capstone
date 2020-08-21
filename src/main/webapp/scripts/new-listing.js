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

  $(document).ready(function(){
    const previewHash = '#preview';
    const $preview = $(previewHash);
    const bottom = $preview.position().top + $preview.offset().top + 
        $preview.outerHeight(true);
    $('html, body').animate({
        scrollTop: bottom
    }, 900, function() {
      window.location.hash = previewHash;
    });
  });
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

/**
 * Checks if a text field is filled correctly and updates the page accordingly.
 * If a field is invalid, it and its instructions will be highlighted and
 * submissions will be disabled. If a field is made valid the highlights will
 * be removed and there will be a check to enable submission buttons.
 *
 * @param fieldId the id of the field being modified
 * @param instructionsId the id of the instructions to be followed for a field
 */
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

/**
 *
 */
function suggestTags() {
  const suggestionContainer = document.getElementById('tags-suggestion-container');
  let currentSuggestions = suggestionContainer.childNodes;
  let currentTags = document.getElementById('cause-tags').value.split(',');
  for (let i = 0; i < currentTags.length; i++) {
    currentTags[i] = currentTags[i].trim();
  }
  console.log('current tags', currentTags);
  
  // Get location words
  const locationInfo = document.getElementById('cause-location').value.trim().toLowerCase().split(',');

  // Get the words mentioned in the other fields
  // TODO(zrideaux@): expand regex for international characters
  let words = [];
  words = words.concat(
      document.getElementById('cause-name').value.trim().toLowerCase().split(/[^A-Za-z0-9]/));
  words = words.concat(
      document.getElementById('cause-description').value.trim().toLowerCase().split(/[^A-Za-z0-9]/));
  words = words.concat(
      document.getElementById('cause-how-to-help').value.trim().toLowerCase().split(/[^A-Za-z0-9]/));

  // Remove common from words
  const wordsToRemove = ['and', 'a', 'an', 'the', 'at', 'to', 'this', 'is', 'in', 'for'];
  words = words.filter((word) => {
    if (!wordsToRemove.includes(word)) {
      return word;
    }
  });
  console.log(words);

  // Get word counts
  let wordCounts = new Map();
  for (let i = 0; i < words.length; i++) {
    if (wordCounts.has(words[i])) {
      wordCounts.set(words[i], wordCounts.get(words[i]) + 1);
    } else {
      wordCounts.set(words[i], 1);
    }
  }

  console.log(wordCounts);

  // TODO(zrideaux@): Prevent users from adding tag suggestion if it would make
  //     value exceed maximum limit

  // Add location tags
  for (let i = 0; i < locationInfo.length; i++) {
    let newSuggestion = createSuggestionElement(locationInfo[i].trim());

    // NEED TO TRIM TAGS

    if ((!suggestionContainer.innerHTML.includes(newSuggestion.outerHTML) &&
        (!currentTags.includes(locationInfo[i].trim())))) {
      suggestionContainer.appendChild(newSuggestion);
    }
  }
  
  wordCounts.forEach((value, key, map) => {
    let newSuggestion = createSuggestionElement(key);

    if ((value > 1) &&
        (!suggestionContainer.innerHTML.includes(newSuggestion.outerHTML)) &&
        (!currentTags.includes(key))) {
      suggestionContainer.appendChild(newSuggestion);
    }
  });
}

function createSuggestionElement(text) {
  let suggestionElement = document.createElement('button');
  
  suggestionElement.innerText = '+ ' + text;
  suggestionElement.className = 'tag-suggestion';
  suggestionElement.onclick = () => {
    let currentTags = document.getElementById('cause-tags').value;
    if (currentTags.charAt(currentTags.length - 1) === ',' ||
        currentTags.charAt(currentTags.length - 1) === '') {
      document.getElementById('cause-tags').value += text;
    } else {
      document.getElementById('cause-tags').value += ', ' + text;
    }
    suggestionElement.remove();
  }

  return suggestionElement;
}

/**
 * Performs a check on all of the page's input fields to determine if
 * submissions should be enabled.
 */
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

/**
 * Disables submission buttons and makes an instruction message visible.
 */
function disableSubmissions() {
  let submissionButtons = document.getElementsByClassName('submission-buttons');
  for (let i = 0; i < submissionButtons.length; i++) {
    submissionButtons[i].setAttribute('disabled', '');
  }
  let stillNeededSpan = document.getElementById('still-needed');
  stillNeededSpan.style.visibility = "visible";
}

/**
 * Enables submission buttons and makes an instruction message hidden.
 */
function enableSubmissions() {
  let submissionButtons = document.getElementsByClassName('submission-buttons');
  for (let i = 0; i < submissionButtons.length; i++) {
    submissionButtons[i].removeAttribute('disabled');
  }
  let stillNeededSpan = document.getElementById('still-needed');
  stillNeededSpan.style.visibility = "hidden";
}

export {
  createNewListing,
  displayPreviewListing,
  suggestTags,
  validateInput
};
