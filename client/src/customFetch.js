export default async function customFetch(url, body) {
  const myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");

  const jsonBody = JSON.stringify(body);

  const requestOptions = {
    method: "POST",
    headers: myHeaders,
    body: jsonBody,
    redirect: "follow",
  };

  // function handleErrors(response) {
  //   if (!response.ok) {
  //     throw Error(response.statusText);
  //   }
  //   return response;
  // }

  return fetch(url, requestOptions);
}
