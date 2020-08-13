import { 
  createDivElement
} from './htmlElement.js';

/**
 *  Appends a loader to a container element.
 * 
 * @param containerElement The element to append this loader to.
 * @param idAttribute The name of the id of this loader element 
 */
export default function addLoader(containerElement, idAttribute) {
  containerElement.appendChild(
    createDivElement('', 'loader', idAttribute));
    console.log("Added loader");
}