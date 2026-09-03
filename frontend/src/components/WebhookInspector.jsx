import { useEffect, useMemo, useState } from 'react'

function getWsUrl() {
  const configured = import.meta.env.VITE_WEBSOCKET_URL
  if (configured) {
    return configured
  }

  const backendUrl = import.meta.env.VITE_BACKEND_URL || 'http://localhost:8080'
  const isSecure = backendUrl.startsWith('https://')
  return backendUrl.replace(isSecure ? 'https://' : 'http://', isSecure ? 'wss://' : 'ws://') + '/ws/webhooks'
}

export default function WebhookInspector() {
  const [messages, setMessages] = useState([])
  const [status, setStatus] = useState('connecting')
  const wsUrl = useMemo(() => getWsUrl(), [])

  useEffect(() => {
    const socket = new WebSocket(wsUrl)

    socket.onopen = () => setStatus('connected')
    socket.onclose = () => setStatus('disconnected')
    socket.onerror = () => setStatus('error')
    socket.onmessage = (event) => {
      const entry = {
        id: `${Date.now()}-${Math.random()}`,
        receivedAt: new Date().toLocaleTimeString(),
        payload: event.data,
      }
      setMessages((current) => [entry, ...current].slice(0, 100))
    }

    return () => socket.close()
  }, [wsUrl])

  return (
    <section className="rounded-xl border border-slate-200 bg-white p-5 shadow-sm">
      <div className="mb-3 flex items-center justify-between">
        <h2 className="text-lg font-semibold">Webhook Inspector</h2>
        <span className="rounded bg-slate-100 px-2 py-1 text-xs font-medium uppercase">{status}</span>
      </div>
      <div className="max-h-80 space-y-3 overflow-auto">
        {messages.length === 0 ? (
          <p className="text-sm text-slate-500">Waiting for incoming webhook payloads...</p>
        ) : (
          messages.map((msg) => (
            <article key={msg.id} className="rounded border border-slate-200 bg-slate-50 p-3">
              <p className="mb-2 text-xs text-slate-500">{msg.receivedAt}</p>
              <pre className="overflow-auto text-xs text-slate-800">{msg.payload}</pre>
            </article>
          ))
        )}
      </div>
    </section>
  )
}
