// --- API Service ---
const API_BASE_URL = 'http://localhost:8080';

const apiService = {
  getProduits: async (params = {}) => {
    try {
      const searchParams = new URLSearchParams(params);
      const response = await fetch(`${API_BASE_URL}/produits?${searchParams}`);
      if (!response.ok) throw new Error('Erreur lors du chargement des produits');
      return response.json();
    } catch (error) {
      showMessage('error', 'Erreur API produits');
      return { content: [], totalPages: 0 };
    }
  },
  getCategories: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/produits/categories`);
      if (!response.ok) throw new Error('Erreur lors du chargement des catégories');
      return response.json();
    } catch (error) {
      showMessage('error', 'Erreur API catégories');
      return [];
    }
  },
  createProduit: async (produit) => {
    try {
      const response = await fetch(`${API_BASE_URL}/produits`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': 'Basic ' + btoa('admin:admin123') },
        body: JSON.stringify(produit)
      });
      if (!response.ok) throw new Error('Erreur lors de la création');
      return response.json();
    } catch (error) {
      showMessage('error', 'Erreur API création produit');
      throw error;
    }
  },
  updateProduit: async (id, produit) => {
    try {
      const response = await fetch(`${API_BASE_URL}/produits/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json', 'Authorization': 'Basic ' + btoa('admin:admin123') },
        body: JSON.stringify(produit)
      });
      if (!response.ok) throw new Error('Erreur lors de la mise à jour');
      return response.json();
    } catch (error) {
      showMessage('error', 'Erreur API update produit');
      throw error;
    }
  },
  deleteProduit: async (id) => {
    try {
      const response = await fetch(`${API_BASE_URL}/produits/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': 'Basic ' + btoa('admin:admin123') }
      });
      return response.ok;
    } catch (error) {
      showMessage('error', 'Erreur API suppression produit');
      return false;
    }
  },
  getCommandes: async (params = {}) => {
    try {
      const searchParams = new URLSearchParams(params);
      const response = await fetch(`${API_BASE_URL}/commandes?${searchParams}`);
      if (!response.ok) throw new Error('Erreur lors du chargement des commandes');
      return response.json();
    } catch (error) {
      showMessage('error', 'Erreur API commandes');
      return { content: [], totalPages: 0 };
    }
  },
  createCommande: async (commande) => {
    try {
      const response = await fetch(`${API_BASE_URL}/commandes`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(commande)
      });
      if (!response.ok) throw new Error('Erreur lors de la création de la commande');
      return response.json();
    } catch (error) {
      showMessage('error', 'Erreur API création commande');
      throw error;
    }
  }
};

// --- Navigation ---
const sections = {
  dashboard: document.getElementById('dashboard-section'),
  produits: document.getElementById('produits-section'),
  commandes: document.getElementById('commandes-section'),
  panier: document.getElementById('panier-section')
};

function showSection(section) {
  Object.keys(sections).forEach(key => {
    sections[key].style.display = (key === section) ? '' : 'none';
  });
  document.querySelectorAll('.nav-links a').forEach(link => {
    link.classList.toggle('active', link.id === `nav-${section}`);
  });
}

// --- Message ---
function showMessage(type, text) {
  const container = document.getElementById('message-container');
  const msg = document.createElement('div');
  msg.className = `message ${type}`;
  msg.textContent = text;
  container.appendChild(msg);
  setTimeout(() => msg.remove(), 4000);
}

// --- Dashboard ---
async function renderDashboard() {
  const produitsData = await apiService.getProduits({ page: 0, size: 100 });
  const commandesData = await apiService.getCommandes({ page: 0, size: 100 });
  const produits = produitsData.content || [];
  const commandes = commandesData.content || [];
  const panier = JSON.parse(localStorage.getItem('panier') || '[]');

  sections.dashboard.innerHTML = `
    <div class="grid grid-3">
      <div class="card">
        <div class="flex space-between">
          <div>
            <p class="text-muted">Total Produits</p>
            <p class="stat">${produits.length}</p>
          </div>
        </div>
      </div>
      <div class="card">
        <div class="flex space-between">
          <div>
            <p class="text-muted">Commandes</p>
            <p class="stat">${commandes.length}</p>
          </div>
        </div>
      </div>
      <div class="card">
        <div class="flex space-between">
          <div>
            <p class="text-muted">Panier</p>
            <p class="stat">${panier.length}</p>
          </div>
        </div>
      </div>
    </div>
    <div class="card">
      <h3>Dernières Commandes</h3>
      <table>
        <thead>
          <tr>
            <th>ID</th><th>Client</th><th>Email</th><th>Date</th><th>Total</th>
          </tr>
        </thead>
        <tbody>
          ${commandes.slice(0, 5).map(c => `
            <tr>
              <td>#${c.id}</td>
              <td>${c.nomClient}</td>
              <td>${c.emailClient}</td>
              <td>${c.dateCommande ? new Date(c.dateCommande).toLocaleDateString('fr-FR') : ''}</td>
              <td>${c.total ? c.total.toFixed(2) : ''} FCFA</td>
            </tr>
          `).join('')}
        </tbody>
      </table>
      ${commandes.length === 0 ? '<div class="text-center text-muted">Aucune commande disponible</div>' : ''}
    </div>
  `;
}

// --- Produits ---
async function renderProduits() {
  const produitsData = await apiService.getProduits({ page: 0, size: 100 });
  const produits = produitsData.content || [];
  const categories = await apiService.getCategories();

  sections.produits.innerHTML = `
    <div class="card">
      <div class="flex space-between" style="align-items:center;">
        <h2>Produits</h2>
        <button class="btn" onclick="showProductModal()">Nouveau Produit</button>
      </div>
      <div style="margin: 16px 0;">
        <input type="text" id="search-produit" placeholder="Rechercher..." style="max-width:200px;">
        <select id="filter-categorie" style="max-width:200px; margin-left:8px;">
          <option value="">Toutes les catégories</option>
          ${categories.map(cat => `<option value="${cat}">${cat}</option>`).join('')}
        </select>
      </div>
      <div class="grid grid-4" id="produits-list">
        ${produits.map(p => `
          <div class="card">
            <h3>${p.nom}</h3>
            <p>${p.description || ''}</p>
            <div><b>${p.prix.toFixed(2)} FCFA</b> | Stock: ${p.stock}</div>
            <div style="margin-top:8px;">
              <button class="btn btn-secondary" onclick="addToCart(${p.id})">Ajouter au panier</button>
              <button class="btn" onclick="editProduit(${p.id})">Modifier</button>
              <button class="btn btn-danger" onclick="deleteProduit(${p.id})">Supprimer</button>
            </div>
          </div>
        `).join('')}
      </div>
    </div>
  `;
}

// --- Commandes ---
async function renderCommandes() {
  const commandesData = await apiService.getCommandes({ page: 0, size: 100 });
  const commandes = commandesData.content || [];
  sections.commandes.innerHTML = `
    <div class="card">
      <h2>Commandes</h2>
      <table>
        <thead>
          <tr>
            <th>ID</th><th>Client</th><th>Email</th><th>Date</th><th>Total</th>
          </tr>
        </thead>
        <tbody>
          ${commandes.map(c => `
            <tr>
              <td>#${c.id}</td>
              <td>${c.nomClient}</td>
              <td>${c.emailClient}</td>
              <td>${c.dateCommande ? new Date(c.dateCommande).toLocaleDateString('fr-FR') : ''}</td>
              <td>${c.total ? c.total.toFixed(2) : ''} FCFA</td>
            </tr>
          `).join('')}
        </tbody>
      </table>
      ${commandes.length === 0 ? '<div class="text-center text-muted">Aucune commande disponible</div>' : ''}
    </div>
  `;
}

// --- Panier ---
function renderPanier() {
  const panier = JSON.parse(localStorage.getItem('panier') || '[]');
  sections.panier.innerHTML = `
    <div class="card">
      <h2>Mon Panier</h2>
      ${panier.length === 0 ? '<div class="text-muted">Votre panier est vide</div>' : `
        <table>
          <thead>
            <tr>
              <th>Produit</th>
              <th>Prix</th>
              <th>Quantité</th>
              <th>Total</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            ${panier.map(item => `
              <tr>
                <td>${item.nom}</td>
                <td>${item.prix.toFixed(2)} FCFA</td>
                <td>${item.quantite}</td>
                <td>${(item.prix * item.quantite).toFixed(2)} FCFA</td>
                <td><button class="btn btn-danger" onclick="removeFromCart(${item.id})">Retirer</button></td>
              </tr>
            `).join('')}
          </tbody>
        </table>
        <div style="margin-top:16px;">
          <b>Total : ${panier.reduce((t, i) => t + i.prix * i.quantite, 0).toFixed(2)} FCFA</b>
          <button class="btn" onclick="showOrderModal()" style="margin-left:16px;">Passer la commande</button>
        </div>
      `}
    </div>
  `;
}

// --- Fonctions Panier ---
window.addToCart = function(id) {
  apiService.getProduits().then(data => {
    const produit = (data.content || []).find(p => p.id === id);
    if (!produit) return showMessage('error', 'Produit introuvable');
    let panier = JSON.parse(localStorage.getItem('panier') || '[]');
    const exist = panier.find(item => item.id === id);
    if (exist) {
      if (exist.quantite < produit.stock) {
        exist.quantite++;
      } else {
        return showMessage('error', 'Stock insuffisant');
      }
    } else {
      panier.push({ ...produit, quantite: 1 });
    }
    localStorage.setItem('panier', JSON.stringify(panier));
    showMessage('success', 'Produit ajouté au panier');
    renderPanier();
  });
};

window.removeFromCart = function(id) {
  let panier = JSON.parse(localStorage.getItem('panier') || '[]');
  panier = panier.filter(item => item.id !== id);
  localStorage.setItem('panier', JSON.stringify(panier));
  showMessage('info', 'Produit retiré du panier');
  renderPanier();
};

// --- Modals (à compléter selon besoins) ---
// --- MODAL PRODUIT ---
window.showProductModal = function(editId = null) {
  let produit = {
    nom: '',
    description: '',
    prix: '',
    stock: '',
    categorie: ''
  };
  if (editId) {
    apiService.getProduits().then(data => {
      const found = (data.content || []).find(p => p.id === editId);
      if (found) {
        produit = found;
        renderProductModal(produit, true);
      } else {
        showMessage('error', 'Produit introuvable');
      }
    });
  } else {
    renderProductModal(produit, false);
  }
};

function renderProductModal(produit, isEdit) {
  const modal = document.getElementById('modal-container');
  modal.classList.add('active');
  modal.innerHTML = `
    <div class="modal">
      <button class="close-btn" onclick="closeModal()">&times;</button>
      <h2>${isEdit ? 'Modifier' : 'Nouveau'} Produit</h2>
      <form id="produit-form" autocomplete="off">
        <label>Nom <span style="color:red">*</span></label>
        <input type="text" name="nom" value="${produit.nom || ''}" required maxlength="100">
        <label>Description</label>
        <textarea name="description" rows="2" maxlength="500">${produit.description || ''}</textarea>
        <label>Prix (FCFA) <span style="color:red">*</span></label>
        <input type="number" name="prix" min="0.01" step="0.01" value="${produit.prix || ''}" required>
        <label>Stock <span style="color:red">*</span></label>
        <input type="number" name="stock" min="0" step="1" value="${produit.stock || ''}" required>
        <label>Catégorie <span style="color:red">*</span></label>
        <input type="text" name="categorie" value="${produit.categorie || ''}" required maxlength="100">
        <div id="form-error" style="color:red;margin:8px 0 0 0;"></div>
        <button type="submit" class="btn" style="margin-top:16px;width:100%">${isEdit ? 'Mettre à jour' : 'Créer'}</button>
      </form>
    </div>
  `;
  document.getElementById('produit-form').onsubmit = async function(e) {
    e.preventDefault();
    const formData = new FormData(this);
    const data = {
      nom: formData.get('nom').trim(),
      description: formData.get('description').trim(),
      prix: parseFloat(formData.get('prix')),
      stock: parseInt(formData.get('stock')),
      categorie: formData.get('categorie').trim()
    };
    // Validation avancée
    let error = '';
    if (!data.nom || data.nom.length < 2) error = 'Le nom est requis (min 2 caractères).';
    else if (data.nom.length > 100) error = 'Le nom ne doit pas dépasser 100 caractères.';
    else if (data.description.length > 500) error = 'La description ne doit pas dépasser 500 caractères.';
    else if (isNaN(data.prix) || data.prix <= 0) error = 'Le prix doit être un nombre positif.';
    else if (isNaN(data.stock) || data.stock < 0) error = 'Le stock doit être un entier positif ou nul.';
    else if (!data.categorie || data.categorie.length < 2) error = 'La catégorie est requise (min 2 caractères).';
    else if (data.categorie.length > 100) error = 'La catégorie ne doit pas dépasser 100 caractères.';
    if (error) {
      document.getElementById('form-error').textContent = error;
      return;
    }
    try {
      if (isEdit) {
        await apiService.updateProduit(produit.id, data);
        showMessage('success', 'Produit mis à jour');
      } else {
        await apiService.createProduit(data);
        showMessage('success', 'Produit créé');
      }
      closeModal();
      renderProduits();
    } catch (err) {
      document.getElementById('form-error').textContent = 'Erreur lors de la sauvegarde.';
    }
  };
}

window.closeModal = function() {
  const modal = document.getElementById('modal-container');
  modal.classList.remove('active');
  modal.innerHTML = '';
};

window.editProduit = function(id) {
  showProductModal(id);
};
window.deleteProduit = function(id) {
  if (confirm('Supprimer ce produit ?')) {
    apiService.deleteProduit(id).then(success => {
      if (success) {
        showMessage('success', 'Produit supprimé');
        renderProduits();
      } else {
        showMessage('error', 'Erreur suppression');
      }
    });
  }
};
window.showOrderModal = function() {
  const panier = JSON.parse(localStorage.getItem('panier') || '[]');
  const modal = document.getElementById('modal-container');
  modal.classList.add('active');
  modal.innerHTML = `
    <div class="modal">
      <button class="close-btn" onclick="closeModal()">&times;</button>
      <h2>Passer une commande</h2>
      <form id="commande-form" autocomplete="off">
        <label>Nom du client <span style="color:red">*</span></label>
        <input type="text" name="nomClient" required maxlength="100">
        <label>Email du client <span style="color:red">*</span></label>
        <input type="email" name="emailClient" required maxlength="100">
        <div style="margin:12px 0;">
          <b>Produits commandés :</b><br>
          ${panier.length === 0 ? '<span style="color:red">Votre panier est vide.</span>' :
            `<ul style='margin:8px 0 0 16px;padding:0;'>${panier.map(item => `<li>${item.nom} x${item.quantite} (${item.prix.toFixed(2)} FCFA)</li>`).join('')}</ul>`}
        </div>
        <div id="commande-error" style="color:red;margin:8px 0 0 0;"></div>
        <button type="submit" class="btn" style="margin-top:16px;width:100%">Valider la commande</button>
      </form>
    </div>
  `;
  document.getElementById('commande-form').onsubmit = async function(e) {
    e.preventDefault();
    const formData = new FormData(this);
    const nomClient = formData.get('nomClient').trim();
    const emailClient = formData.get('emailClient').trim();
    // Validation avancée
    let error = '';
    if (!nomClient || nomClient.length < 2) error = 'Le nom du client est requis (min 2 caractères).';
    else if (nomClient.length > 100) error = 'Le nom ne doit pas dépasser 100 caractères.';
    else if (!emailClient) error = 'L\'email du client est requis.';
    else if (!/^\S+@\S+\.\S+$/.test(emailClient)) error = 'L\'email doit être valide.';
    else if (panier.length === 0) error = 'Votre panier est vide.';
    if (error) {
      document.getElementById('commande-error').textContent = error;
      return;
    }
    try {
      const commande = {
        nomClient,
        emailClient,
        produitIds: panier.map(item => item.id)
      };
      await apiService.createCommande(commande);
      localStorage.setItem('panier', '[]');
      showMessage('success', 'Commande créée avec succès ! Un email de confirmation vous a été envoyé.');
      closeModal();
      renderPanier();
      renderCommandes();
    } catch (err) {
      document.getElementById('commande-error').textContent = 'Erreur lors de la création de la commande.';
    }
  };
};

// --- Navigation Events ---
document.getElementById('nav-dashboard').onclick = (e) => {
  e.preventDefault();
  showSection('dashboard');
  renderDashboard();
};
document.getElementById('nav-produits').onclick = (e) => {
  e.preventDefault();
  showSection('produits');
  renderProduits();
};
document.getElementById('nav-commandes').onclick = (e) => {
  e.preventDefault();
  showSection('commandes');
  renderCommandes();
};
document.getElementById('nav-panier').onclick = (e) => {
  e.preventDefault();
  showSection('panier');
  renderPanier();
};

// --- Initialisation ---
window.onload = () => {
  showSection('dashboard');
  renderDashboard();
};
