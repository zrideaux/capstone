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

export { suggestTags };