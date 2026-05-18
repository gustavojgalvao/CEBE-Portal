// In-memory data for demonstration purposes
const supportRequests = [];

// RF07: Solicitações de atendimento ou dúvidas
exports.createSupportRequest = (req, res) => {
  const { name, phone, email, subject, message } = req.body;
  
  if (!name || !subject || !message) {
    return res.status(400).json({ error: 'Nome, assunto e mensagem são obrigatórios.' });
  }
  
  const newRequest = {
    id: supportRequests.length + 1,
    name,
    phone,
    email,
    subject,
    message,
    status: 'Pendente',
    createdAt: new Date().toISOString()
  };
  
  supportRequests.push(newRequest);
  
  // Simulação de envio de confirmação automática mencionada na Solução
  console.log(`[Auto-Reply] Mensagem de confirmação enviada para ${phone || email}`);
  
  res.status(201).json({ message: 'Solicitação enviada com sucesso. Entraremos em contato em breve.', request: newRequest });
};

// Auxiliar: Listar solicitações
exports.getAllSupportRequests = (req, res) => {
  res.status(200).json(supportRequests);
};
