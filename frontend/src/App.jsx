import axios from 'axios'
import { useCallback, useEffect, useMemo, useState } from 'react'
import WebhookInspector from './components/WebhookInspector'

const METHODS = ['GET', 'POST', 'PUT', 'PATCH', 'DELETE']

function normalizePath(path) {
  if (!path.startsWith('/')) {
    return `/${path}`
  }
  return path
}

function App() {
  const apiBase = import.meta.env.VITE_BACKEND_URL || 'http://localhost:8080'
  const client = useMemo(() => axios.create({ baseURL: apiBase }), [apiBase])

  const [path, setPath] = useState('/api/example')
  const [httpMethod, setHttpMethod] = useState('GET')
  const [responseStatus, setResponseStatus] = useState(200)
  const [delayMs, setDelayMs] = useState(0)
  const [responseBody, setResponseBody] = useState('{"message":"ok"}')
  const [routes, setRoutes] = useState([])
  const [error, setError] = useState('')
  const [saving, setSaving] = useState(false)

  const refreshRoutes = useCallback(async () => {
    try {
      const { data } = await client.get('/api/mock-routes')
      setRoutes(data)
    } catch {
      setRoutes([])
    }
  }, [client])

  useEffect(() => {
    refreshRoutes()
  }, [refreshRoutes])

  const handleSubmit = async (event) => {
    event.preventDefault()
    setError('')

    try {
      JSON.parse(responseBody)
    } catch {
      setError('Response JSON must be valid before saving.')
      return
    }

    setSaving(true)
    try {
      await client.post('/api/mock-routes', {
        path: normalizePath(path.trim()),
        httpMethod,
        responseStatus: Number(responseStatus),
        delayMs: Number(delayMs),
        responseBody,
        responseHeaders: {
          'Content-Type': 'application/json',
        },
      })
      await refreshRoutes()
    } catch {
      setError('Failed to save route. Ensure backend is running.')
    } finally {
      setSaving(false)
    }
  }

  return (
    <main className="min-h-screen bg-slate-50 px-4 py-8 text-slate-900">
      <div className="mx-auto grid w-full max-w-6xl gap-6 lg:grid-cols-2">
        <section className="rounded-xl border border-slate-200 bg-white p-5 shadow-sm">
          <h1 className="mb-4 text-2xl font-bold">MockPulse Endpoint Builder</h1>
          <form className="space-y-3" onSubmit={handleSubmit}>
            <label className="block">
              <span className="mb-1 block text-sm font-medium">Path</span>
              <input className="w-full rounded border border-slate-300 px-3 py-2" value={path} onChange={(event) => setPath(event.target.value)} required />
            </label>

            <div className="grid grid-cols-2 gap-3">
              <label className="block">
                <span className="mb-1 block text-sm font-medium">Method</span>
                <select className="w-full rounded border border-slate-300 px-3 py-2" value={httpMethod} onChange={(event) => setHttpMethod(event.target.value)}>
                  {METHODS.map((method) => (
                    <option key={method} value={method}>{method}</option>
                  ))}
                </select>
              </label>
              <label className="block">
                <span className="mb-1 block text-sm font-medium">Status</span>
                <input className="w-full rounded border border-slate-300 px-3 py-2" type="number" min="100" max="599" value={responseStatus} onChange={(event) => setResponseStatus(event.target.value)} required />
              </label>
            </div>

            <label className="block">
              <span className="mb-1 block text-sm font-medium">Delay (ms)</span>
              <input className="w-full rounded border border-slate-300 px-3 py-2" type="number" min="0" value={delayMs} onChange={(event) => setDelayMs(event.target.value)} required />
            </label>

            <label className="block">
              <span className="mb-1 block text-sm font-medium">JSON Response</span>
              <textarea className="min-h-36 w-full rounded border border-slate-300 px-3 py-2 font-mono text-sm" value={responseBody} onChange={(event) => setResponseBody(event.target.value)} required />
            </label>

            {error && <p className="text-sm text-red-600">{error}</p>}

            <button className="rounded bg-slate-900 px-4 py-2 text-sm font-medium text-white disabled:opacity-50" type="submit" disabled={saving}>
              {saving ? 'Saving...' : 'Save Endpoint'}
            </button>
          </form>
        </section>

        <WebhookInspector />

        <section className="rounded-xl border border-slate-200 bg-white p-5 shadow-sm lg:col-span-2">
          <h2 className="mb-3 text-lg font-semibold">Configured Routes</h2>
          {routes.length === 0 ? (
            <p className="text-sm text-slate-500">No routes configured yet.</p>
          ) : (
            <div className="overflow-auto">
              <table className="w-full border-collapse text-left text-sm">
                <thead>
                  <tr className="border-b border-slate-200 text-slate-600">
                    <th className="py-2">Method</th>
                    <th className="py-2">Path</th>
                    <th className="py-2">Status</th>
                    <th className="py-2">Delay</th>
                  </tr>
                </thead>
                <tbody>
                  {routes.map((route) => (
                    <tr key={route.id} className="border-b border-slate-100">
                      <td className="py-2 font-medium">{route.httpMethod}</td>
                      <td className="py-2 font-mono">{route.path}</td>
                      <td className="py-2">{route.responseStatus}</td>
                      <td className="py-2">{route.delayMs}ms</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>
      </div>
    </main>
  )
}

export default App
