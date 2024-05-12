import http from 'k6/http';

export const options = {
  // A number specifying the number of VUs to run concurrently.
  vus: 100,
  // A string specifying the total duration of the test run.
  duration: '120s',
  // duration: '30s',
};

// The function that defines VU logic.
//
// See https://grafana.com/docs/k6/latest/examples/get-started-with-k6/ to learn more
// about authoring k6 scripts.
//
export default function() {
  // const url = 'http://localhost:9080/person/sync';
  const url = 'http://localhost:9080/person/async';
  const payload = JSON.stringify({
    firstName: 'firstName',
    middleName: 'midName',
    lastName: 'lastName',
    gender: 'X',
    birthDate: '2000-01-01'
  });
  const params = {
    headers: {
      'Content-Type': 'application/json'
    }
  };

  http.post(url, payload, params);
}
