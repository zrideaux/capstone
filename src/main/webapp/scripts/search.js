import { 
  getListings
} from './listing.js';

import { 
  getCheckboxesByName,
  getRadioByName
} from './miscellaneous.js';

export default function displayListings(containerElement) {
  const queryString = '/fetch-listings?' + getSearchParameters();
  getListings(containerElement, '', 'search-listings', queryString);
}

/**
 */
function getSearchParameters() {
  const filterTypes = getCheckboxesByName('search-type-option');
  console.log("FILTER type: " + filterTypes);
  console.log("LENGTH filter type: " + filterTypes.length);
  const filterRadius = getRadioByName('search-radius-option');
  const sort = getRadioByName('search-sort-option');
  let param = 'type-filters=' + filterTypes;
  param += '&radius-filter' + filterRadius;
  param += '&sort' + sort;
  return param;
}
