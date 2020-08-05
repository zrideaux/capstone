import { 
  getListings
} from './listing.js';

export default function displayListings(containerElement) {
  const queryString = '/fetch-listings';
  getListings(containerElement, '', 'search-listings', queryString);
}
