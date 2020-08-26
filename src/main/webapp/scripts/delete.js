import {
  createDivElement,
  createPElement,
  createSpanElement
} from './htmlElement.js';

import {
  displayErrorMessage,
  isErrorMessage,
  isSuccessMessage,
  keyboardAccessible,
  toggleElementDisplay
} from './miscellaneous.js';

/**
 * Create a delete element. 
 *
 * @param deleteButtonClass the class to give to the delete button.
 * @param deleteOnSuccessdFunc the function to call when the deletion is
 *     successful.
 * @param name the name of the object that will be deleted.
 * @param query a String that represents a query.
 * @return A span element that represents a delete element.
 */
function createDelete(deleteButtonClass, deleteOnSuccessdFunc, name, query) {
  const deleteContainerDiv = createDivElement('', '', '');

  const deleteAlertDiv = createDeleteModal(deleteOnSuccessdFunc, name,
      query);
  deleteContainerDiv.appendChild(createDeleteButton(deleteAlertDiv,
      deleteButtonClass));

  deleteContainerDiv.appendChild(deleteAlertDiv);

  return deleteContainerDiv;
}

/**
 * Creates a delete button that when clicked, will create a confirm deletion 
 *     modal.
 *
 * @param deleteAlertDiv the confirm deletion modal to toggle.
 * @param deleteButtonClass the class to give to the delete button.
 * @return an element that represents a delete button.
 */
function createDeleteButton(deleteAlertDiv, deleteButtonClass) {
  const deleteSpan = createSpanElement('delete', 'delete-icon material-icons ' +
      deleteButtonClass, '');

  const toggleDeleteModalFunc = (event) => { 
    // Create pop up asking if user is sure they want to delete the object.
    toggleElementDisplay('flex', deleteAlertDiv);
    event.stopPropagation();
  };

  keyboardAccessible(deleteSpan, toggleDeleteModalFunc);

  return deleteSpan;
}

/**
 * Creates an alert or a confirm deletion modal to confirm that the user wants 
 *     to delete the object.
 *
 * @param deleteOnSuccessdFunc the function to call when the deletion is
 *     successful.
 * @param name the name of the entity to be deleted.
 * @param query a String that represents a query.
 * @return an element that represents a deletion modal.
 */
function createDeleteModal(deleteOnSuccessdFunc, name, query) {
  const deleteModalDiv = createDivElement('', 'delete-modal modal', '');

  const stopPropogationFunc = (event) => {
    event.stopPropagation();
  }
  keyboardAccessible(deleteModalDiv, stopPropogationFunc);

  const deleteContainerDiv = createDivElement('', 'delete-container', '');
  deleteModalDiv.appendChild(deleteContainerDiv);

  deleteContainerDiv.appendChild(
    createSpanElement('error_outline', 'material-icons modal-delete-icon', ''));

  const errorMessage = createPElement('Are you sure you want to delete ', '',
      '')
  errorMessage.innerHTML = errorMessage.innerHTML + 
      '<span class="delete-name">"' + name + '"</span>';
  deleteContainerDiv.appendChild(errorMessage);

  const buttonContainerDiv = createDivElement('', 'delete-button-container', '');
  deleteContainerDiv.appendChild(buttonContainerDiv);

  buttonContainerDiv.appendChild(createDeleteModalDeleteButton(
      deleteOnSuccessdFunc, query));
  buttonContainerDiv.appendChild(createDeleteModalCancelButton(deleteModalDiv));

  return deleteModalDiv;
}

/**
 * Creates a delete button for the delete modal that will delete an entity when 
 *     clicked.
 *
 * @param deleteOnSuccessdFunc the function that will be called when the entity 
 *     is deleted.
 * @param query a String that represents a query.
 * @return an element that represetns a delete button.
 */
function createDeleteModalDeleteButton(deleteOnSuccessdFunc, query) {
  // const deleteButton = createButtonElement('Delete', '', '');
  const deleteButton = createDivElement('', 'delete-button delete-modal-button',
      '');
  deleteButton.innerText = 'Delete';

  const deleteEntityFunc = () => {
    // Delete the object.
    deleteEntity(deleteOnSuccessdFunc, query);
  };

  keyboardAccessible(deleteButton, deleteEntityFunc);

  return deleteButton;
}

/**
 * Creates a cancel button for the delete modal.
 *
 * @param deleteModalDiv toggles the display of the modal if the button is 
 *     clicked.
 * @return an element that represents a cancel button.
 */
function createDeleteModalCancelButton(deleteModalDiv) {
  const cancelButton = createDivElement('', 'cancel-button delete-modal-button',
      '');
  cancelButton.innerText = 'Cancel';

  const toggleDeleteModalFunc = () => {
    // Create pop up asking if user is sure they want to delete the object.
    toggleElementDisplay('flex', deleteModalDiv);
  };

  keyboardAccessible(cancelButton, toggleDeleteModalFunc);

  return cancelButton;
}

/**
 * Deletes an object.
 * If the deletion was successful, call the onSuccessFunc.
 *
 * @param onSuccessFunc the function to call when the deletion is successful.
 * @param query a String that represents a query.
 */
function deleteEntity(onSuccessFunc, query) {
  fetch(query, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'}
    })
        .then(response => response.json())
        .then((message) => {
          if (isErrorMessage(message)) {
            displayErrorMessage(message);
          } else if (isSuccessMessage(message)) {
            onSuccessFunc();
          } else {
            displayErrorMessage('');
          }
        })
}

export { createDelete };
