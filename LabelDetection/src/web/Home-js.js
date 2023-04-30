// Image Upload Function
function uploadImage() {
    const fileInput = document.getElementById("image-upload");
    const file = fileInput.files[0];
    const formData = new FormData();
    formData.append('file', file);
  
    // Send FormData to server using fetch API or XMLHttpRequest
    // Example using fetch API:
    fetch("http://localhost:8080/get-labels", {
      method: "POST",
      body: formData,
    })
    .then(response => {
      console.log("Image uploaded successfully.");
    })
    .catch(error => {
      console.error("Error uploading image:", error);
    });
  }
  
  // PDF Upload Function
  function uploadPDF() {
    const fileInput = document.getElementById("pdf-upload");
    const file = fileInput.files[0];
    const formData = new FormData();
    formData.append("file", file);
  
    // Send FormData to server using fetch API or XMLHttpRequest
    // Example using fetch API:
    fetch("http://localhost:8080/get-labels", {
      method: "POST",
      body: formData,
    })
    .then(response => {
      console.log("PDF uploaded successfully.");
    })
    .catch(error => {
      console.error("Error uploading PDF:", error);
    });
  }
  