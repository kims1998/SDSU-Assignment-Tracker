import { useEffect } from "react";
import { createPortal } from "react-dom";

export default function ConfirmDialog(
    {
        open,
        title = "Delete this assignment?",
        message = "This action cannot be undone.",
        confirmText = "Delete",
        cancelText = "Cancel",
        onConfirm,
        onClose,
        busy = false, // disable buttons while waiting
        error = null,
    }) {
    useEffect(() => {
        if (!open) return;
        const onKey = (e) => e.key === "Escape" && !busy && onClose();
        window.addEventListener("keydown", onKey);
        return () => window.removeEventListener("keydown", onKey);
    }, [open, busy, onClose]);

    if (!open) return null;

    return createPortal(
        <div className="cd-backdrop" onMouseDown={(e) => {
            if (e.target === e.currentTarget && !busy) onClose();
        }}>
            <div className="cd-card" role="dialog" aria-modal="true" aria-labelledby="cd-title">
                <h3 id="cd-title" className="cd-title">{ title }</h3>
                <p className="cd-body">{ message }</p>
                {error && <p className="cd-error" role="alert">{ error }</p>}
                <div className="cd-actions">
                    <button className="btn btn-secondary" onClick={ onClose } disabled={ busy }>
                        {cancelText}
                    </button>
                    <button className="btn btn-danger" onClick={ onConfirm } disabled={ busy }>
                        {busy ? "Deleting…" : confirmText}
                    </button>
                </div>
            </div>
        </div>,
        document.body
    );
}
