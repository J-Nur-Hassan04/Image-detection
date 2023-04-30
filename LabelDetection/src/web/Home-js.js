
async function uploadImage() {
  const form = document.getElementById('file-upload-form');
  const fileInput = document.getElementById('file-input');
  const submitBtn = document.getElementById('submit-btn');
  const resultTable = document.getElementById('result-table');

  const file = fileInput.files[0];
  const formData = new FormData();
  formData.append('file', file);

  const promisResponse = await fetch('http://localhost:8080/get-labels', {
    method: 'POST',
    body: formData
  });

  var response = await promisResponse.json();
  let resultHtml = `<tr>
                      <th>Image</th>
                      <th>Labels</th>
                      <th>Texts</th>
                    </tr>`;
  let paraLabels = ``;
  let paraTexts = ``;

  for (let label of response.resBody.labels) {
    paraLabels += `<p>${"Label Name : " + label.label}<br>${"Confidence : " + label.confidence}</p>`;
  }
  for (let text of response.resBody.texts) {
    paraTexts += `<p>${"Detected Text : " + text.detectedText}<br> ${"Confidence : " + text.confidence}</p>`;
  }

  resultHtml += `<tr> 
                  <td><img src=${response.resBody.image} width="200" height="200"></img></td>
                  <td>${paraLabels}</td>
                  <td>${paraTexts}</td>          
                </tr>`;
  resultTable.innerHTML = resultHtml;
}

async function uploadPdf() {
  const form = document.getElementById('file-upload-form-pdf');
  const fileInput = document.getElementById('file-input-pdf');
  const submitBtn = document.getElementById('submit-btn-pdf');
  const resultTable = document.getElementById('result-table');

  const file = fileInput.files[0];
  const formData = new FormData();
  formData.append('file', file);

  const promisResponse = await fetch('http://localhost:8080/get-labels', {
    method: 'POST',
    body: formData
  });

  var response = await promisResponse.json();
  let resultHtml = `<tr>
                      <th>Image</th>
                      <th>Labels</th>
                      <th>Texts</th>
                    </tr>`;

  for (let resB of response.resBody) {
    let paraLabels = ``;
    let paraTexts = ``;

    for (let label of resB.labels) {
      paraLabels += `<p>${"Label Name : " + label.label}<br>${"Confidence : " + label.confidence}</p>`;
    }
    for (let text of resB.texts) {
      paraTexts += `<p>${"Detected Text : " + text.detectedText}<br> ${"Confidence : " + text.confidence}</p>`;
    }
    resultHtml += `<tr> 
                      <td><img src=${resB.image} width="200" height="200"></img></td>
                      <td>${paraLabels}</td>
                      <td>${paraTexts}</td>          
                    </tr>`;
  }
  resultTable.innerHTML = resultHtml;
}
