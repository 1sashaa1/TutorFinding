<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ru">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Управление клиентами</title>
	<!-- Bootstrap 5 CSS -->
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
	<!-- Font Awesome для иконок -->
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
	<!-- Custom CSS -->
	<style>

		.table-responsive {
			max-height: calc(100vh - 150px);
			overflow-y: auto;
		}
		.action-btn {
			padding: 0.25rem 0.5rem;
			font-size: 0.875rem;
		}

		@media (min-width: 576px) {
			.ms-sm-auto {
				margin-left: 155px !important;
			}
		}
	</style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
	<div class="container-fluid">
		<a class="navbar-brand" href="#">
			<i class="fas fa-users me-2"></i>Управление клиентами
		</a>
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="navbarNav">
			<ul class="navbar-nav ms-auto">
				<li class="nav-item">
					<a class="nav-link" href="Dashboard"><i class="fas fa-home me-1"></i>Главная</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="logout"><i class="fas fa-sign-out-alt me-1"></i>Выйти</a>
				</li>
			</ul>
		</div>
	</div>
</nav>

<div class="container-fluid">
	<div class="row">

		<div class="col-md-9 col-lg-10 ms-sm-auto px-md-4 py-3">
			<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
				<h1 class="h2">Список клиентов</h1>
				<div class="btn-toolbar mb-2 mb-md-0">
					<button type="button" class="btn btn-sm btn-success" data-bs-toggle="modal" data-bs-target="#createClientModal">
						<i class="fas fa-plus me-1"></i>Добавить клиента
					</button>
				</div>
			</div>

			<div class="card mb-4">
				<div class="card-body">
					<div class="row">
						<div class="col-md-6">
							<div class="input-group mb-3">
								<span class="input-group-text"><i class="fas fa-search"></i></span>
								<input type="text" class="form-control" placeholder="Поиск клиентов...">
								<button class="btn btn-primary" type="button">Найти</button>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="table-responsive">
				<table class="table table-striped table-hover">
					<thead class="table-dark">
					<tr>
						<th scope="col">#</th>
						<th scope="col">Имя</th>
						<th scope="col">Email</th>
						<th scope="col">Дата регистрации</th>
						<th scope="col">Действия</th>
					</tr>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${empty clients}">
							<tr>
								<td colspan="7" class="text-center py-4">Нет данных о клиентах</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach var="client" items="${clients}" varStatus="loop">
								<tr data-client-id="${client.id}">
									<th scope="row">${loop.index + 1}</th>
									<td>${client.name}</td>
									<td>${client.email}</td>
									<td>
										${client.created_at}
									</td>
									<td>
										<div class="btn-group btn-group-sm" role="group">
											<button type="button" class="btn btn-outline-primary action-btn"
													data-bs-toggle="tooltip" data-bs-placement="top" title="Просмотр">
												<i class="fas fa-eye"></i>
											</button>
											<button type="button" class="btn btn-outline-success action-btn"
													data-bs-toggle="modal" data-bs-placement="top" data-bs-target="#editClientModal" title="Редактировать">
												<i class="fas fa-edit"></i>
											</button>
											<button type="button" class="btn btn-outline-danger action-btn"
													data-bs-toggle="tooltip" data-bs-placement="top" title="Удалить"
													onclick="confirmDelete(${client.id}, '${client.name}')">
												<i class="fas fa-trash-alt"></i>
											</button>
										</div>
									</td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
					</tbody>
				</table>
			</div>

			<nav aria-label="Page navigation">
				<ul class="pagination justify-content-center mt-4">
					<li class="page-item disabled">
						<a class="page-link" href="#" tabindex="-1">Предыдущая</a>
					</li>
					<li class="page-item active"><a class="page-link" href="#">1</a></li>
					<li class="page-item"><a class="page-link" href="#">2</a></li>
					<li class="page-item"><a class="page-link" href="#">3</a></li>
					<li class="page-item">
						<a class="page-link" href="#">Следующая</a>
					</li>
				</ul>
			</nav>
		</div>
	</div>
</div>

<div class="modal fade" id="deleteModal" tabindex="-1" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title">Подтверждение удаления</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
			</div>
			<div class="modal-body">
				Вы уверены, что хотите удалить клиента <span id="clientName" class="fw-bold"></span>?
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Отмена</button>
				<button type="button" class="btn btn-danger" id="confirmDeleteBtn">Удалить</button>
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="createClientModal" tabindex="-1" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title">Добавление нового клиента</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
			</div>
			<form id="createClientForm">
				<div class="modal-body">
					<div class="mb-3">
						<label for="clientName" class="form-label">Имя</label>
						<input type="text" class="form-control" id="Name" required>
					</div>
					<div class="mb-3">
						<label for="clientEmail" class="form-label">Email</label>
						<input type="email" class="form-control" id="clientEmail" required>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Отмена</button>
					<button type="submit" class="btn btn-primary" id="saveClientBtn">Сохранить</button>
				</div>
			</form>
		</div>
	</div>
</div>

<!-- Модальное окно редактирования -->
<div class="modal fade" id="editClientModal" tabindex="-1" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title">Редактирование клиента</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
			</div>
			<form id="editClientForm">
				<div class="modal-body">
					<input type="hidden" id="editClientId">
					<div class="mb-3">
						<label for="editClientName" class="form-label">Имя</label>
						<input type="text" class="form-control" id="editClientName" required>
					</div>
					<div class="mb-3">
						<label for="editClientEmail" class="form-label">Email</label>
						<input type="email" class="form-control" id="editClientEmail" required>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Отмена</button>
					<button type="submit" class="btn btn-primary" id="updateClientBtn">Сохранить изменения</button>
				</div>
			</form>
		</div>
	</div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
	$(document).ready(function() {
		// Инициализация tooltips
		$('[data-bs-toggle="tooltip"]').tooltip();

		// Обработчик кнопки удаления
		$('#confirmDeleteBtn').click(function() {
			const btn = $(this);
			btn.html('<span class="spinner-border spinner-border-sm"></span> Удаление...').prop('disabled', true);

			$.ajax({
				url: 'deleteUser/' + currentClientId,
				type: 'DELETE',
				success: function(data) {
					console.log(data.message)
					loadClientsData();
					if (data.message) {
						showToast('Успех', data.message, 'success');
						$('#deleteModal').modal('hide');
					} else {
						showToast('Ошибка', data.message || 'Ошибка при удалении', 'danger');
					}
				},
				error: function(xhr) {
					const errorMsg = xhr.responseJSON?.message || 'Не удалось удалить клиента';
					showToast('Ошибка', errorMsg, 'danger');
				},
				complete: function() {
					btn.html('Удалить').prop('disabled', false);
				}
			});
		});

		// Функция загрузки данных клиентов
		function loadClientsData() {
			$.ajax({
				url: '/admin/getClients',
				type: 'GET',
				success: function(data) {
					console.log(data); // Печатаем весь ответ от сервера

					if (data.success) {
						// Получаем список клиентов
						const clients = data.clients;
						const clientCount = data.count;

						console.log("Количество клиентов: " + clientCount);
						console.log("Список клиентов:", clients);

						// Пример: обновление таблицы с клиентами
						updateClientsTable(clients);
					} else {
						console.log("Ошибка: " + (data.message || "Не удалось получить клиентов"));
					}
				},
				error: function(xhr, status, error) {
					console.log("Ошибка запроса: " + error);
				}
			});
		}

	});

	let currentClientId = null;

	function confirmDelete(clientId, clientName) {
		currentClientId = clientId;
		$('#clientName').text(clientName);
		$('#deleteModal').modal('show');
	}

	function updateClientsTable(clients) {
		const tbody = $('tbody');
		tbody.empty();

		if (!clients || clients.length === 0) {
			tbody.append('<tr><td colspan="5" class="text-center py-4">Нет данных о клиентах</td></tr>');
			return;
		}

		console.log(clients); // Проверка данных перед рендером

		clients.forEach((client, index) => {
			tbody.append(
					`<tr>
            <th scope="row">${index + 1}</th>
            <td>${client.name}</td>
            <td>${client.email}</td>
            <td>${client.created_at}</td>
            <td>
                <div class="btn-group btn-group-sm" role="group">
                    <button type="button" class="btn btn-outline-primary action-btn" data-bs-toggle="tooltip" data-bs-placement="top" title="Просмотр">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button type="button" class="btn btn-outline-success action-btn" data-bs-toggle="tooltip" data-bs-placement="top" title="Редактировать">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button type="button" class="btn btn-outline-danger action-btn" data-bs-toggle="tooltip" data-bs-placement="top" title="Удалить"
                            onClick="confirmDelete(${client.id}, '${client.name}')">
                        <i class="fas fa-trash-alt"></i>
                    </button>
                </div>
            </td>
        </tr>`);
		});
	}

	function showToast(title, message, type) {
		console.log(`${title}: ${message}`);
	}
	$(document).ready(function() {
		// Обработка отправки формы
		$('#createClientForm').submit(function(e) {
			e.preventDefault();
			createClient();
		});
	});

	function createClient() {
		const btn = $('#saveClientBtn');
		btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span> Сохранение...');

		const clientData = {
			name: $('#Name').val().trim(),
			email: $('#clientEmail').val().trim(),
		};

		if (!clientData.name || !clientData.email) {
			showToast('Ошибка', 'Заполните обязательные поля', 'danger');
			btn.prop('disabled', false).html('Сохранить');
			return;
		}

		$.ajax({
			url: '/createClient',
			type: 'POST',
			contentType: 'application/json',
			data: JSON.stringify(clientData),
			success: function(response) {
				$('#createClientModal').modal('hide');
				showToast('Успех', 'Клиент успешно создан', 'success');
				resetForm();
				updateClientsTable(); // Обновляем таблицу
			},
			error: function(xhr) {
				const errorMsg = xhr.responseJSON?.message || 'Ошибка при создании клиента';
				showToast('Ошибка', errorMsg, 'danger');
			},
			complete: function() {
				btn.prop('disabled', false).html('Сохранить');
			}
		});
	}

	function resetForm() {
		$('#createClientForm')[0].reset();
		$('#createClientForm').find('.is-invalid').removeClass('is-invalid');
	}

	// Обработчик кнопки редактирования
	$(document).on('click', '.btn-outline-success.action-btn', function() {
		const clientId = $(this).closest('tr').data('client-id');
		console.log("Передаём id в форму " + clientId);
		loadClientData(clientId);
	});

	// Загрузка данных клиента для редактирования
	function loadClientData(clientId) {
		console.log("Показали форму с clientId " + clientId);
		$.get(`/admin/api/clients/` + clientId, function(client) {
			console.log("Показали форму с данными " + client.name);
			$('#editClientId').val(client.id);
			$('#editClientName').val(client.name);
			$('#editClientEmail').val(client.email);
			$('#editClientModal').modal('show');
		}).fail(function() {
			showToast('Ошибка', 'Не удалось загрузить данные клиента', 'danger');
		});
	}

	// Обработка сохранения изменений
	$('#editClientForm').submit(function(e) {
		e.preventDefault();
		updateClient();
	});

	function updateClient() {
		const btn = $('#updateClientBtn');
		btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span> Сохранение...');

		const clientData = {
			id: $('#editClientId').val().trim(),
			name: $('#editClientName').val().trim(),
			email: $('#editClientEmail').val().trim(),
		};

		$.ajax({
			url: `/admin/api/clients/` + clientData.id,
			type: 'PUT',
			contentType: 'application/json',
			data: JSON.stringify(clientData),
			success: function(response) {
				$('#editClientModal').modal('hide');
				showToast('Успех', 'Данные клиента обновлены', 'success');
				updateClientsTable(); // Обновляем таблицу
			},
			error: function(xhr) {
				const errorMsg = xhr.responseJSON?.message || 'Ошибка при обновлении данных';
				showToast('Ошибка', errorMsg, 'danger');
			},
			complete: function() {
				btn.prop('disabled', false).html('Сохранить изменения');
			}
		});
	}
	$(function () {
		$('[data-bs-toggle="modal"]').tooltip({
			trigger: 'hover',
			placement: 'top'
		});
	});

</script>
</body>
</html>