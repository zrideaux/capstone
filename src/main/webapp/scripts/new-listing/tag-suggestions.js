import { createButtonElement } from './../htmlElement.js';

import validateInput from './validate-input.js';

const wordsToRemove = ['a', 'an', 'and', 'at', 'for', 'get', 'in', 'is', 'the',
    'this', 'to'];

// TODO(zrideaux@): add suggestion for state associated with state code 
//                  filled in location field
const stateCodes = {
  'ca': 'california'
}

/**
 * Use information filled into the forms on the new listing page to generate
 * clickable tag suggestions for a new listing.
 */
function suggestTags() {
  // Get each of the current tags and existing suggestions
  const suggestionContainer = document.getElementById('tags-suggestion-container');
  let currentSuggestions = suggestionContainer.childNodes;
  let currentTags = document.getElementById('cause-tags').value.split(',');
  for (let i = 0; i < currentTags.length; i++) {
    currentTags[i] = currentTags[i].trim();
  }
  
  // Get location words
  let locationInfo = document.getElementById(
      'cause-location').value.trim().toLowerCase().split(',');

  // Get the words mentioned in the other fields
  // TODO(zrideaux@): expand regex for international characters
  let words = [];
  words = words.concat(
      document.getElementById('cause-name').value.trim().toLowerCase().split(/[^A-Za-z0-9]/));
  words = words.concat(
      document.getElementById('cause-description').value.trim().toLowerCase().split(/[^A-Za-z0-9]/));
  words = words.concat(
      document.getElementById('cause-how-to-help').value.trim().toLowerCase().split(/[^A-Za-z0-9]/));

  // Remove common/nondescript terms from words array
  words = words.filter((word) => {
    if (!wordsToRemove.includes(word)) {
      return word;
    }
  });

  // Map each word in words array to the number of times it appears
  let wordCounts = getWordCounts(words);

  // TODO(zrideaux@): Prevent users from adding tag suggestion if it would make
  //                  value exceed maximum limit

  // Add location tags
  populateLocationSuggestions(currentTags, locationInfo, suggestionContainer);

  // Add suggestions based on inputs in other boxes
  let populateKeywordSuggestions = createPopulateKeywordSuggestions(
      currentTags, suggestionContainer);
  wordCounts.forEach(populateKeywordSuggestions);
}

/**
 * Given an array of words, create and return a map that maps each word to the
 * number of times it appears in the array.
 *
 * @param words an array of words
 * @return a map of words as keys and the number of times they appear in the
 *    words array as their values
 */
function getWordCounts(words) {
  let wordCounts = new Map();
  
  for (let i = 0; i < words.length; i++) {
    if (wordCounts.has(words[i])) {
      wordCounts.set(words[i], wordCounts.get(words[i]) + 1);
    } else {
      wordCounts.set(words[i], 1);
    }
  }

  return wordCounts;
}

/** 
 * Populate the suggestionContainer with location related tag suggestions.
 *
 * @param currentTags an array of existing tags to compare new suggestions with
 * @param locationInfo an array of location information from the location input
 * @param suggestionContainer the element to add new suggestions to
 */
function populateLocationSuggestions(currentTags, locationInfo, suggestionContainer) {
  for (let i = 0; i < locationInfo.length; i++) {
    let newSuggestion = createSuggestionElement(locationInfo[i].trim());

    if ((!suggestionContainer.innerHTML.includes(newSuggestion.outerHTML) &&
        (!currentTags.includes(locationInfo[i].trim())))) {
      suggestionContainer.appendChild(newSuggestion);
    }
  }
}

/**
 * Create a function to populate the suggestionContainer with suggestions
 * determined from the non-location input fields with a map of word counts.
 *
 * @param currentTags an array of existing tags to compare new suggestions with
 * @param suggestionContainer the element to add new suggestions to
 * @return a function that adds new suggestions to a container
 */
function createPopulateKeywordSuggestions(currentTags, suggestionContainer) {
  return (value, key, map) => {
    let newSuggestion = createSuggestionElement(key);

    if ((value > 1) &&
        (!suggestionContainer.innerHTML.includes(newSuggestion.outerHTML)) &&
        (!currentTags.includes(key))) {
      suggestionContainer.appendChild(newSuggestion);
    }
  }
}

/**
 * Creates and returns a new button element that adds a tag to the tags input
 * when clicked.
 *
 * @param text the text that should fill the button and be added when clicked
 * @return a button element of a suggestion
 */
function createSuggestionElement(text) {
  let suggestionElement = createButtonElement('+ ' + text, 'tag-suggestion', '');

  suggestionElement.onclick = () => {
    // Determine if suggestion should be added with a comma 
    let currentTags = document.getElementById('cause-tags').value.trim();
    let lastCharacter = currentTags.charAt(currentTags.length - 1);
    if (lastCharacter === ',' || lastCharacter === '') {
      document.getElementById('cause-tags').value =
          document.getElementById('cause-tags').value.trim() + ' ' + text;
    } else {
      document.getElementById('cause-tags').value = 
          document.getElementById('cause-tags').value.trim() + ', ' + text;
    }

    suggestionElement.remove();
  }

  return suggestionElement;
}

export { suggestTags };
