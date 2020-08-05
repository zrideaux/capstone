import { getListings } from './listing.js';

import { 
  getCheckboxesByName,
  getRadioByName
} from './miscellaneous.js';

/**
 * Displays listings based on the search parameters (includes type filters, 
 *     radius filter, and sortBy).
 */
export default function displayListings() {
  const containerElement = document.getElementById("listings");
  containerElement.innerHTML = '';
  const queryString = '/fetch-listings?' + getSearchParameters();
  getListings(containerElement, '', 'search-listings', queryString);
}

/**
 * Get the search parameters (type filters, radius filter, and sortBy) from the 
 *     search page.
 */
function getSearchParameters() {
  const filterTypes = getCheckboxesByName('search-type-option');
  const filterRadius = getRadioByName('search-radius-option');
  const sort = getRadioByName('search-sort-option');
  let param = 'type-filters=' + filterTypes;
  param += '&radius-filter=' + filterRadius;
  param += '&sortBy=' + sort;
  return param;
}
