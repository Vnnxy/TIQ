// nominatim search should be replaced with a local database.
// Local database should cache nominatim searches

export async function geocodeCity(query)
{
  const url =
    'https://nominatim.openstreetmap.org/search' +
    `?q=${encodeURIComponent(query)}` +
    '&format=json' +
    '&limit=1'

  const res = await fetch(url,
    {headers: { 'User-Agent': 'rug-web-eng-group-44' }})

  const data = await res.json()

  if (!data.length) {
    throw new Error('Location not found')
  }

  return {
    lat: Number(data[0].lat),
    lon: Number(data[0].lon)
  }
}
